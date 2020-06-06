#include <SoftwareSerial.h>
 
// Initializing communication ports

// motors
const int rightBackward = 2;
const int rightForward = 3;
const int leftBackward = 4;
const int leftForward = 5;

// COlor sensor
const int clrS0 = 6;
const int clrS1 = 7;
const int clrS2 = 8;
const int clrS3 = 9;
const int clrOut = 12;

// Stores frequency read by the photodiodes
int redFrequency = 0;
int greenFrequency = 0;
int blueFrequency = 0;


// bluetooth
const int inpt = 10;
const int outpt = 11;
SoftwareSerial hc06(outpt, inpt); // TX/RX pins

#define ledPin 13
#define printRGB false

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
  
  // Setting the sensorOut as an input
  pinMode(outpt, INPUT);
  pinMode(inpt, OUTPUT);

  //Putting S0/S1 on HIGH/HIGH levels means the output frequency scalling is at 100%
  //LOW/LOW is off HIGH/LOW is 20% and LOW/HIGH is  2%
  digitalWrite(clrS0,HIGH);
  digitalWrite(clrS1,LOW);
  
   // Begins serial communication 
  Serial.begin(9600);
  
  // --- MOTORS ---------------------------------------
  pinMode(leftBackward, OUTPUT);
 pinMode(leftForward, OUTPUT);
  pinMode(rightBackward, OUTPUT);
   pinMode(rightForward, OUTPUT);

  // --- BLUETOOTH ------------------------------------
  pinMode(ledPin, OUTPUT);
  hc06.begin(9600);
}

void loop() {
  if (hc06.available()){
    int val = hc06.read();
    Serial.print((char)val);
    if      (val == '1') {
       // start free style walking
       freestyle = 1;
       goForward();  
    }
    else if (val == '5') {
      goForward();
      freestyle = 0;
    }
    else if (val == '6') { 
      goBackward();
      freestyle = 0;
    }
    else if (val == '7') {
      TurnLeft();
      freestyle = 0;
    }
    else if (val == '8') { 
      TurnRight();
      freestyle = 0;
    }
    else if (val == '9') { 
      dontMove();
      freestyle = 0;
    } else {
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
}
