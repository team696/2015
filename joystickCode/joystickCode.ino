/*
Arduino Leonardo Joystick!

*/


JoyState_t joySt;
int buttons = 0;
double oldX = 0;
double x = 0;

void setup()
{
        Serial.begin(9600);
        
        for (int i = 2;i<14;i++){
          pinMode(i,INPUT_PULLUP);
        }
        
	joySt.xAxis = 0;
	joySt.yAxis = 0;
	joySt.zAxis = 0;
	joySt.xRotAxis = 0;
	joySt.yRotAxis = 0;
//	joySt.zRotAxis = 0;
//	joySt.throttle = 0;
//	joySt.rudder = 0;
//	joySt.hatSw1;
//	joySt.hatSw2;
	joySt.buttons = 0;
}


void loop()
{
        joySt.xAxis=map(analogRead(A0),0,1023,0,255);
        joySt.yAxis=map(analogRead(A1),0,1023,0,255);
        joySt.zAxis=map(analogRead(A2),0,1023,0,255);
        joySt.xRotAxis=map(analogRead(A3),0,1023,0,255);
        joySt.yRotAxis=map(analogRead(A4),0,1023,0,255);
        int button=analogRead(A3);
//	joySt.throttle++;
        buttons = 0;
//        boolean isPushed = button<200;
//        Serial.print(isPushed);
//        Serial.print("   ");
        if(button>650){
          buttons+=1;
        }
        if(digitalRead(2)==LOW){
          buttons+=2;
        }
       if(digitalRead(3)==LOW){
          buttons+=4;
        }
       if(digitalRead(4)==LOW){
          buttons+=8;
        }
       if(digitalRead(5)==LOW){
          buttons+=16;
        }
       if(digitalRead(6)==LOW){
          buttons+=32;
        } 
        if(digitalRead(7)==LOW){
          buttons+=64;
        }
        if(digitalRead(8)==LOW){
          buttons+=128;
        }
        if(digitalRead(9)==LOW){
          buttons+=256;
        }
        if(digitalRead(10)==LOW){
          buttons+=512;
        }
        if(digitalRead(11)==LOW){
          buttons+=1024;
        }
        if(digitalRead(12)==LOW){
          buttons+=2048;
        }
        if(digitalRead(13)==LOW){
          buttons+=4096;
        }
        
        
//	joySt.buttons <<= 1;
//	if (joySt.buttons == 0)
//		joySt.buttons = 1;
//        if(joySt.buttons == 1)
//                joySt.buttons = 0;
        
        joySt.buttons = buttons;
	// Call Joystick.move
	Joystick.setState(&joySt);

}
