//#include <SoftwareSerial.h>

// motors
#define rightBackward 22
#define rightForward 24
#define leftBackward 26
#define leftForward 28

// Color sensor
#define clrS0 30
#define clrS1 32
#define clrS2 34
#define clrS3 36
#define clrOut 38

// Lift up-down
#define lifterUp 42
#define lifterDown 44

// log
#define ledPin 13
#define printRGB true
#define printMSG false

// Stores frequency read by the photodiodes
int redFrequency = 0;
int greenFrequency = 0;
int blueFrequency = 0;

 // free style
 int freestyle = 0;
  
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
  Serial2.begin(9600);
  
  // --- LOGING ---------------------------------------
  Serial.begin(9600);  
  pinMode(ledPin, OUTPUT);
}

void loop() {
  if (Serial2.available() > 0){
    int val = Serial2.read();
    if (printMSG) Serial.print("got ");
    if (printMSG) Serial.print((char)val);
    if (printMSG) Serial.print("!!!\n");
    
    if (val == '1') {
       // start free style walking
      if (printMSG) Serial.print("1!!!\n");
       freestyle = 1;
       goForward();  
    }
    else if (val == '5') {
      if (printMSG) Serial.print("5!!!\n");
      goForward();
      freestyle = 0;
    }
    else if (val == '6') { 
      if (printMSG) Serial.print("6!!!\n");
      goBackward();
      freestyle = 0;
    }
    else if (val == '7') {
      if (printMSG) Serial.print("7!!!\n");
      TurnLeft();
      freestyle = 0;
    }
    else if (val == '8') { 
      if (printMSG) Serial.print("8!!!\n");
      TurnRight();
      freestyle = 0;
    }
    else if (val == 'U') { 
      if (printMSG) Serial.print("U!!!\n");
      forkliftUp();
      freestyle = 0;
    }
    else if (val == 'D') { 
      if (printMSG) Serial.print("D!!!\n");
      forkliftDown();
      freestyle = 0;
    }
    else if (val == '9') { 
      if (printMSG) Serial.print("9!!!\n");
      dontMove();
      freestyle = 0;
    } else {
      if (printMSG) Serial.print("else!!!\n");
      digitalWrite(ledPin , HIGH);
      delay(100);
      digitalWrite(ledPin , LOW);
      delay(100);
      digitalWrite(ledPin , HIGH);
      delay(300);
      digitalWrite(ledPin , LOW);
      delay(100);
      digitalWrite(ledPin , HIGH);
      delay(100);
      digitalWrite(ledPin , LOW);
    }
  } else if (freestyle == 1) {
    // on black ground - turn around
    if (redFrequency >= 120    &&
        greenFrequency >= 120 &&
        blueFrequency >= 130) {
        TurnRight();
        delay(1300);
        goForward();
    } 
    // on blue ground - turn left
    if (redFrequency >= 65      && redFrequency <= 85    &&
        greenFrequency >= 65    && greenFrequency <= 85  &&
        ((blueFrequency >= 35   && blueFrequency <= 55) || (blueFrequency >= 60 && blueFrequency <= 95) ) ) {
        TurnLeft();
        delay(850);
        goForward();
    }
  }
  
  // ============= DELAY =================
  readColor();
}

void readColor(){
    // ============= COLOR =================

  // Setting RED (R) filtered photodiodes to be read
  digitalWrite(clrS2,LOW);
  digitalWrite(clrS3,LOW);
  delay(100);
  
  // Reading the output frequency
  redFrequency = pulseIn(clrOut, LOW);
  //redFrequency = map(redFrequency, 25,72,255,0);
  
   // Printing the RED (R) value
  if (printRGB) Serial.print("R = ");
  if (printRGB) Serial.print(redFrequency);
  
  // Setting GREEN (G) filtered photodiodes to be read
  digitalWrite(clrS2,HIGH);
  digitalWrite(clrS3,HIGH);
  delay(50);
  
  // Reading the output frequency
  greenFrequency = pulseIn(clrOut, LOW);
  //greenFrequency = map(greenFrequency, 30,90,255,0);
  
  // Printing the GREEN (G) value  
  if (printRGB) Serial.print(" G = ");
  if (printRGB) Serial.print(greenFrequency);
 
  // Setting BLUE (B) filtered photodiodes to be read
  digitalWrite(clrS2,LOW);
  digitalWrite(clrS3,HIGH);
  delay(50);
  
  // Reading the output frequency
  blueFrequency = pulseIn(clrOut, LOW);
  //blueFrequency = map(blueFrequency, 25,70,255,0);
  
  // Printing the BLUE (B) value 
  if (printRGB) Serial.print(" B = ");
  if (printRGB) Serial.println(blueFrequency);
  
}

void forkliftUp()
{
  digitalWrite(lifterUp , HIGH);
  digitalWrite(lifterDown , LOW);
}

void forkliftDown()
{
  digitalWrite(lifterUp , LOW);
  digitalWrite(lifterDown , HIGH);
}

void goForward()
{
  digitalWrite(leftForward , HIGH);
  digitalWrite(leftBackward , LOW);
  digitalWrite(rightForward , HIGH);
  digitalWrite(rightBackward , LOW);
}

void goBackward()
{
  digitalWrite(leftForward , LOW);
  digitalWrite(leftBackward , HIGH);
  digitalWrite(rightForward , LOW);
  digitalWrite(rightBackward , HIGH);
}


void TurnRight()
{
  digitalWrite(leftForward , HIGH);
  digitalWrite(leftBackward , LOW);
  digitalWrite(rightForward , LOW);
  digitalWrite(rightBackward , HIGH);
}

void TurnLeft()
{
  digitalWrite(leftForward , LOW);
  digitalWrite(leftBackward , HIGH);
  digitalWrite(rightForward , HIGH);
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