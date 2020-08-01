#include <SoftwareSerial.h>
#include <NfcAdapter.h>
#include <PN532_HSU.h>
#include <string.h>
#include <PN532.h>

// Command chars
#define CMD_MOVE_FORWARD  '1'
#define CMD_MOVE_BACKWARD '2'
#define CMD_TURN_LEFT     '3'
#define CMD_TURN_RIGHT    '4'
#define CMD_TURN_U        '5'
#define CMD_FORK_UP       '6'
#define CMD_FORK_DOWN     '7'
#define CMD_CHECK_EDGE    '8'
#define CMD_CHECK_STATUS  '9'

// motors
#define rightForward  3//2//22
#define rightBackward 2//3//24
#define leftForward   6//5//26
#define leftBackward  5//6//28

// Color sensor
#define clrS0 30
#define clrS1 32
#define clrS2 34
#define clrS3 36
#define clrOut 38
#define COLOR_CHECK_TIMES 8

// Lift up-down
#define lifterUp 45
#define lifterDown 44

// log
#define ledPin 13
#define printRGB true
#define printMSG true

// DELAY TIMINGS
#define HALF_STEP_DELAY     375
#define ONE_STEP_DELAY      750
#define ONE_TURN_DELAY      680

#define FORK_UP_DELAY     450
#define FORK_DOWN_DELAY   180

#define FORK_UP 1
#define FORK_DOWN 0

// Status
int redFrequency = 0;
int greenFrequency = 0;
int blueFrequency = 0;
int IsForkUp;
int box = 0;

// BLUETOOTH
#define BLUETOOTH_TX 53
#define BLUETOOTH_RX 50

SoftwareSerial hc06(BLUETOOTH_RX,BLUETOOTH_TX);

PN532_HSU pn532hsu(Serial3);
PN532 nfc(pn532hsu);


void setup() {
  // --- COLOR SENSOR ---------------------------------
  // Setting the outputs
  pinMode(clrS0, OUTPUT);
  pinMode(clrS1, OUTPUT);
  pinMode(clrS2, OUTPUT);
  pinMode(clrS3, OUTPUT);
  pinMode(clrOut, INPUT);

  // --- COLOR DETECTION -----------------------------
  // Put S0/S1 on HIGH/HIGH levels means the output
  //  frequency scalling is at 100% LOW/LOW is off
  //  HIGH/LOW is 20% and LOW/HIGH is  2%
  digitalWrite(clrS0,HIGH);
  digitalWrite(clrS1,LOW);

  // --- MOTORS ---------------------------------------
  pinMode(leftBackward, OUTPUT);
  pinMode(leftForward, OUTPUT);
  pinMode(rightBackward, OUTPUT);
  pinMode(rightForward, OUTPUT);

  // --- BLUETOOTH ------------------------------------
  hc06.begin(9600);

  // --- LOGING ---------------------------------------
  Serial.begin(9600);
  pinMode(ledPin, OUTPUT);

  // --- FORK -----------------------------------------
  IsForkUp = FORK_DOWN;

  // --- NFC ------------------------------------------
  nfc.begin();

  uint32_t versiondata = nfc.getFirmwareVersion();
  if (! versiondata) {
    Serial.print("Didn't find PN53x board");
    while (1); // halt
  }

  // Got ok data, print it out!
  Serial.print("Found chip PN5"); Serial.println((versiondata>>24) & 0xFF, HEX);
  Serial.print("Firmware ver. "); Serial.print((versiondata>>16) & 0xFF, DEC);
  Serial.print('.'); Serial.println((versiondata>>8) & 0xFF, DEC);

  // Set the max number of retry attempts to read from a card
  // This prevents us from waiting forever for a card, which is
  // the default behaviour of the PN532.
  nfc.setPassiveActivationRetries(0xFF);

  // configure board to read RFID tags
  nfc.SAMConfig();

  Serial.println("Waiting for an ISO14443A card");
  Serial.println("Finish setup!!");

}

void loop() {
  if (hc06.available() > 0){
    //int val = Serial2.read();
    int val = hc06.read();

    if (printMSG) {
      Serial.print("recieved ");
      Serial.println((char)val);
    }

    if (val == CMD_MOVE_FORWARD) {
      if (printMSG) Serial.print("Taking one step forward\n");
      goForward();
      delay(ONE_STEP_DELAY);
      dontMove();
    }
    else if (val == CMD_MOVE_BACKWARD) {
      if (printMSG) Serial.print("Taking one step backwards\n");
      goBackward();
      delay(ONE_STEP_DELAY);
      dontMove();
    }
    else if (val == CMD_TURN_LEFT) {
      if (printMSG) Serial.print("Turning left\n");
      TurnLeft();
      delay(ONE_TURN_DELAY);
      dontMove();
    }
    else if (val == CMD_TURN_RIGHT) {
      if (printMSG) Serial.print("Turning right\n");
      TurnRight();
      delay(ONE_TURN_DELAY);
      dontMove();
    }
    else if (val == CMD_TURN_U) {
      if (printMSG) Serial.print("Turning arround\n");
      TurnRight();
      delay(2 * ONE_TURN_DELAY);
      dontMove();
    }
    // TODO : test this
    else if (val == CMD_FORK_UP) {
      if (printMSG) Serial.print("picking up a box\n");
      if (IsForkUp == FORK_DOWN) {
        goBackward();
        delay(ONE_STEP_DELAY);
        TurnRight();
        delay(2 * ONE_TURN_DELAY);
        goBackward();
        delay(ONE_STEP_DELAY);
        forkliftUp();
        goForward();
        delay(ONE_STEP_DELAY);
        TurnRight();
        delay(2 * ONE_TURN_DELAY);
        goForward();
        delay(ONE_STEP_DELAY);
        dontMove();
      }
      // TODO : Fix this code
      dontMove();
      IsForkUp = FORK_UP;
    }
    else if (val == CMD_FORK_DOWN) {
      if (printMSG) Serial.print("dropping down a box\n");
      if (IsForkUp == FORK_UP) forkliftDown();
      // TODO : Fix this code
      dontMove();
      IsForkUp = FORK_DOWN;
    }
    else if (val == CMD_CHECK_EDGE) {
      if (printMSG) Serial.print("checking if the next tile is an edge\n");
      goForward();
      delay(HALF_STEP_DELAY);
      dontMove();
      readColor();
      goBackward();
      delay(HALF_STEP_DELAY);
      dontMove();
      char str[80];
      sprintf(str, "%d|%d|%d-\0", redFrequency, greenFrequency, blueFrequency);
      hc06.println(str);
    }
    else if (val == CMD_CHECK_STATUS) {
      if (printMSG) Serial.print("got request for status\n");
      // reading color & nfc
      readColor();
      readNFC();
      SendBackStatus();
      if (printMSG) Serial.print("sent status\n");
    }
    else if (printMSG) Serial.print("received unknown command\n");
  }
  //sendColor();
}

void sendColor() {
  char str[80];
  readColor();
  sprintf(str, "%d|%d|%d\0", redFrequency, greenFrequency, blueFrequency);
  hc06.println(str);
}

void SendBackStatus() {
   char str[80];
  sprintf(str, "%d|%d|%d|%d|%d-\0", redFrequency, greenFrequency, blueFrequency, box, IsForkUp);
  hc06.println(str);
  if (printMSG) Serial.println(str);
}

void readColor(){
  redFrequency = 0;
  greenFrequency = 0;
  blueFrequency = 0;

  for (int i = 0; i < COLOR_CHECK_TIMES; i++) {
    // Setting RED (R) filtered photodiodes to be read
    digitalWrite(clrS2,LOW);
    digitalWrite(clrS3,LOW);
    delay(100);

    // Reading the output frequency
    redFrequency += pulseIn(clrOut, LOW);

     // Printing the RED (R) value
    if (printRGB) Serial.print("R = ");
    if (printRGB) Serial.print(redFrequency);

    // Setting GREEN (G) filtered photodiodes to be read
    digitalWrite(clrS2,HIGH);
    digitalWrite(clrS3,HIGH);
    delay(50);

    // Reading the output frequency
    greenFrequency += pulseIn(clrOut, LOW);

    // Printing the GREEN (G) value
    if (printRGB) Serial.print(" G = ");
    if (printRGB) Serial.print(greenFrequency);

    // Setting BLUE (B) filtered photodiodes to be read
    digitalWrite(clrS2,LOW);
    digitalWrite(clrS3,HIGH);
    delay(50);

    // Reading the output frequency
    blueFrequency += pulseIn(clrOut, LOW);

    // Printing the BLUE (B) value
    if (printRGB) Serial.print(" B = ");
    if (printRGB) Serial.println(blueFrequency);
  }

  redFrequency = redFrequency / COLOR_CHECK_TIMES;
  greenFrequency = greenFrequency / COLOR_CHECK_TIMES;
  blueFrequency = blueFrequency / COLOR_CHECK_TIMES;

}

void readNFC() {
  boolean success;
  uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };
  uint8_t uidLength;
  success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, &uid[0], &uidLength);

  if (success) {
    box = 1;
    for (uint8_t i=0; i < uidLength; i++) {
      Serial.print(" 0x");Serial.println(uid[i], HEX);
    }
  }
  else {
    box = 0;
  Serial.println("no box");
  }
}

void forkliftUp()
{
  analogWrite(lifterUp, 140);
  digitalWrite(lifterDown , LOW);
  delay(FORK_UP_DELAY);
  digitalWrite(lifterUp , LOW);
  digitalWrite(lifterDown , LOW);
}

void forkliftDown()
{
  digitalWrite(lifterUp , LOW);
  analogWrite(lifterDown, 130);
  delay(FORK_DOWN_DELAY);
  digitalWrite(lifterUp , LOW);
  digitalWrite(lifterDown , LOW);
}

void goForward()
{
  analogWrite(leftForward, 140);
  digitalWrite(leftBackward , LOW);
  analogWrite(rightForward, 140);
  digitalWrite(rightBackward , LOW);
}

void goBackward()
{
  digitalWrite(leftForward , LOW);
  analogWrite(leftBackward, 140);
  digitalWrite(rightForward , LOW);
  analogWrite(rightBackward, 140);
}

void TurnRight()
{
  analogWrite(leftForward, 140);
  digitalWrite(leftBackward , LOW);
  digitalWrite(rightForward , LOW);
  analogWrite(rightBackward, 140);
}

void TurnLeft()
{
  digitalWrite(leftForward , LOW);
  analogWrite(leftBackward, 140);
  analogWrite(rightForward, 140);
  digitalWrite(rightBackward , LOW);
}
void dontMove()
{
  digitalWrite(leftForward , LOW);
  digitalWrite(leftBackward , LOW);
  digitalWrite(rightForward , LOW);
  digitalWrite(rightBackward , LOW);
  digitalWrite(lifterUp , LOW);
  digitalWrite(lifterDown , LOW);
}