#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <SoftwareSerial.h>

SoftwareSerial BT1(4,2); // TX, RX

// Screen Variables Defines
#define OLED_RESET 4
Adafruit_SSD1306 display(OLED_RESET);

#define NUMFLAKES 10
#define XPOS 0
#define YPOS 1
#define DELTAY 2

/*  Pulse Sensor Amped 1.5    by Joel Murphy and Yury Gitman   http://www.pulsesensor.com Others parts of the code by Raúl Labrat Rodríguez Ángela Prototype

----------------------  Notes ----------------------  ----------------------
This code:
1) Blinks an LED to User's Live Heartbeat   PIN 13
2) Fades an LED to User's Live HeartBeat    PIN 5
3) Determines BPM
4) Determines Temperature 
5) Prints All of the Above to Serial and 0.96" OLED Display
6) Check every moment the vital signs
7) Send data if is necessary to the mobile app by the Bluetooth connection

Read Me:
https://github.com/WorldFamousElectronics/PulseSensor_Amped_Arduino/blob/master/README.md
https://bitbucket.org/raulabrat/proyecto-fin-de-ciclo-2odam-2017/src/8300126806aab8732c939c99acb7bf3956dc50c7/README.md?at=master&fileviewer=file-view-default
 ----------------------       ----------------------  ----------------------
*/

#define PROCESSING_VISUALIZER 1
#define SERIAL_PLOTTER  2

//Screen Variables
const int WIDTH=128;
const int HEIGHT=64;
const int LENGTH=WIDTH;

int x;
int y[LENGTH];

//Temp Sensor Variables
int tempPin = 1;
float lectura, voltaje, temp;

//Pulse sensor Variables
int pulsePin = 0;                 // Pulse Sensor purple wire connected to analog pin 0
int blinkPin = 13;                // pin to blink led at each beat
int fadePin = 5;                  // pin to do fancy classy fading blink at each beat
int fadeRate = 0;                 // used to fade LED on with PWM on fadePin

//Pulse sensor Volatile Variables, used in the interrupt service routine!
volatile int BPM;                   // int that holds raw Analog in 0. updated every 2mS
volatile int Signal;                // holds the incoming raw data
volatile int IBI = 600;             // int that holds the time interval between beats! Must be seeded!
volatile boolean Pulse = false;     // "True" when User's live heartbeat is detected. "False" when not a "live beat".
volatile boolean QS = false;        // becomes true when Arduoino finds a beat.

// SET THE SERIAL OUTPUT TYPE TO YOUR NEEDS
// PROCESSING_VISUALIZER works with Pulse Sensor Processing Visualizer
//      https://github.com/WorldFamousElectronics/PulseSensor_Amped_Processing_Visualizer
// SERIAL_PLOTTER outputs sensor data for viewing with the Arduino Serial Plotter
//      run the Serial Plotter at 115200 baud: Tools/Serial Plotter or Command+L
static int outputType = SERIAL_PLOTTER;


void setup(){
  pinMode(blinkPin,OUTPUT);         // pin that will blink to your heartbeat!
  pinMode(fadePin,OUTPUT);          // pin that will fade to your heartbeat!
  Serial.begin(115200);             // we agree to talk fast!
  BT1.begin(9600);
  display.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  display.display();
  interruptSetup();
  delay(3000);
  display.clearDisplay();
}


//  Where the Magic Happens
void loop(){

  serialOutput() ;

  if (QS == true){     // A Heartbeat Was Found
                       // BPM and IBI have been Determined
                       // Quantified Self "QS" true when arduino finds a heartbeat
        fadeRate = 255;         // Makes the LED Fade Effect Happen
                                // Set 'fadeRate' Variable to 255 to fade LED with pulse
        serialOutputWhenBeatHappens();   // A Beat Happened, Output that to serial.
        QS = false;                      // reset the Quantified Self flag for next time
  }

  ledFadeToBeat();                      // Makes the LED Fade Effect Happen

  lectura = analogRead(tempPin);
  voltaje = lectura * 5.0;
  voltaje /= 1024.0;
  temp = (voltaje - 0.5) * 100;

  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.setCursor(20,0);
  display.clearDisplay();
  display.print(BPM);
  display.print(" PPM");
  display.setCursor(80,0);
  display.print(temp);
  display.print(" ");
  display.write(167);
  display.print("C");
  display.display();
  display.clearDisplay();

  if(BPM >= 180 || BPM <= 15){
    int current_bpm = BPM;
    float current_temp = temp;
    BT1.print("ANGELA1.0#BT#Grave#Infarto#");
    BT1.print(current_temp);
    BT1.print("Cº@");
    BT1.print(current_bpm);
    BT1.print("PPM#FIN\n");
  }
  if(temp >= 30 && temp < 32){
    int current_bpm = BPM;
    float current_temp = temp;
    BT1.print("ANGELA1.0#BT#Grave#Hipotermia#");
    BT1.print(current_temp);
    BT1.print("Cº@");
    BT1.print(current_bpm);
    BT1.print("PPM#FIN\n");
  }
  if(temp >= 32 && temp <= 34){
    int current_bpm = BPM;
    float current_temp = temp;
    BT1.print("ANGELA1.0#BT#Leve#Congelación#");
    BT1.print(current_temp);
    BT1.print("Cº@");
    BT1.print(current_bpm);
    BT1.print("PPM#FIN\n");
  }
  if(temp >= 37.5 && temp <= 40){
    int current_bpm = BPM;
    float current_temp = temp;
    BT1.print("ANGELA1.0#BT#Leve#Fiebre#");
    BT1.print(current_temp);
    BT1.print("Cº@");
    BT1.print(current_bpm);
    BT1.print("PPM#FIN\n"); 
  }
  if(temp >40 && temp <= 44){
    int current_bpm = BPM;
    float current_temp = temp;
    BT1.print("ANGELA1.0#BT#Grave#Fiebre#");
    BT1.print(current_temp);
    BT1.print("Cº@");
    BT1.print(current_bpm);
    BT1.print("PPM#FIN\n");
  }
  if(temp >44 && temp<=50){
    int current_bpm = BPM;
    float current_temp = temp;
    BT1.print("ANGELA1.0#BT#Grave#Insolación#");
    BT1.print(current_temp);
    BT1.print("Cº@");
    BT1.print(current_bpm);
    BT1.print("PPM#FIN\n");
  }
  delay(2000);                             //  take a break
}
/*
String getMessage(){
  String msg = "";
  char a;
  
  while(BT1.available()) {
      a = BT1.read();
      msg+=String(a);
  }
  return msg;
}
*/
void ledFadeToBeat(){
    fadeRate -= 15;                         //  set LED fade value
    fadeRate = constrain(fadeRate,0,255);   //  keep LED fade value from going into negative numbers!
    analogWrite(fadePin,fadeRate);          //  fade LED
  }
