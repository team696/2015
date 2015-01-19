#pragma config(I2C_Usage, I2C1, i2cSensors)
#pragma config(Sensor, dgtl1,  enc4,           sensorQuadEncoder)
#pragma config(Sensor, dgtl3,  enc1,           sensorQuadEncoder)
#pragma config(Sensor, dgtl5,  enc3,           sensorQuadEncoder)
#pragma config(Sensor, dgtl7,  enc2,           sensorQuadEncoder)
#pragma config(Sensor, I2C_1,  ,               sensorQuadEncoderOnI2CPort,    , AutoAssign)
#pragma config(Sensor, I2C_2,  ,               sensorQuadEncoderOnI2CPort,    , AutoAssign)
#pragma config(Sensor, I2C_3,  ,               sensorQuadEncoderOnI2CPort,    , AutoAssign)
#pragma config(Sensor, I2C_4,  ,               sensorQuadEncoderOnI2CPort,    , AutoAssign)
#pragma config(Motor,  port2,           wheel2,        tmotorVex393_MC29, openLoop, encoderPort, I2C_2)
#pragma config(Motor,  port3,           wheel3,        tmotorVex393_MC29, openLoop, encoderPort, I2C_3)
#pragma config(Motor,  port4,           wheel4,        tmotorVex393_MC29, openLoop, reversed, encoderPort, I2C_4)
#pragma config(Motor,  port5,           swerve1,       tmotorVex393_MC29, openLoop, reversed)
#pragma config(Motor,  port6,           swerve2,       tmotorVex393_MC29, openLoop, reversed)
#pragma config(Motor,  port7,           swerve3,       tmotorVex393_MC29, openLoop, reversed)
#pragma config(Motor,  port8,           swerve4,       tmotorVex393_MC29, openLoop, reversed)
#pragma config(Motor,  port9,           wheel1,        tmotorVex393_MC29, openLoop, reversed, encoderPort, I2C_1)
//*!!Code automatically generated by 'ROBOTC' configuration wizard               !!*//

int cumError=0;
int error=0;
int oldError=0;

float botPos[3]; //x , y and rotation
int lastEncoderCounts[4];

float Kp = 1;
float Ki = 0;
float Kd = 0;

float vectors[4][2]; //each wheel x and y
float values[4][2]; // each wheel speed and angle

int center1 = 0;
int center2 = 0;
int center3 = 0;
int center4 = 0;

int speed1 = 0;
int speed2 = 0;
int speed3 = 0;
int speed4 = 0;

int angle1 = 0;
int angle2 = 0;
int angle3 = 0;
int angle4 = 0;

void resetEnc()
{
	SensorValue[enc1] =0;
	SensorValue[enc2] =0;
	SensorValue[enc3] =0;
	SensorValue[enc4] =0;
}

void resetAll()
{
	motor[wheel1] = 0;
	motor[wheel2] = 0;
	motor[wheel3] = 0;
	motor[wheel4] = 0;

	motor[swerve1] = 0;
	motor[swerve2] = 0;
	motor[swerve3] = 0;
	motor[swerve4] = 0;
}

void trim(){
	if(vexRT[Btn8U] && vexRT[Btn5U]){
			center1++;
	}else if(vexRT[Btn8U] && vexRT[Btn5D]){
			center1--;
	}else if(vexRT[Btn8R] && vexRT[Btn5U]){
			center2++;
	}else if(vexRT[Btn8R] && vexRT[Btn5D]){
			center2--;
	}else if(vexRT[Btn8D] && vexRT[Btn5U]){
			center3++;
	}else if(vexRT[Btn8D] && vexRT[Btn5D]){
			center3--;
	}else if(vexRT[Btn8L] && vexRT[Btn5U]){
			center4++;
	}else if(vexRT[Btn8L] && vexRT[Btn5D]){
			center4--;
	}
}

void calculateVectors(float x, float y, float rotRate){ //rotRate is in radians per second
	for (int fish = 0; fish <4; fish++)
  {
    vectors[fish][0] = x + rotRate*cosDegrees(-45 + fish*90);
    vectors[fish][1] = y + rotRate*sinDegrees(135 + fish*90);
  }
}
void calculateWheelValues(){

	for(int fish = 0; fish<4; fish++)

		{
		values[fish][0] = sqrt((vectors[fish][0] * vectors[fish][0]) + (vectors[fish][1]*vectors[fish][1]));
		values[fish][1] = radiansToDegrees( atan2(vectors[fish][0], vectors[fish][1]));

		if(values[fish][1] > 180){
			values[fish][1] = -(360-values[fish][1]);
		}

		if(values[fish][1] > 90){
			values[fish][1] = -(180-values[fish][1]);
			values[fish][0] = -values[fish][0];
		} else if (values[fish][1] < -90) {
			values[fish][1] = (180+values[fish][1]);
			values[fish][0] = -values[fish][0];
		}

		if(values[fish][0] <0.05 && values[fish][0] >-0.05){
				values[fish][1] = 0;
		}
	}
	float maxValue =0;
	for(int fish = 0; fish<4; fish++){
		if( values[fish][0] > maxValue){
			maxValue = values[fish][0];
		}
	}
	if(maxValue>1){
		for(int fish = 0; fish<4; fish++){
			values[fish][0] = values[fish][0]/maxValue;
		}
	}

}


int deadZone(int in, int min, int max)
{
	if (min < in && in < max)
	{
		return 0;
	}
	return in;
}

int limit(int in, int min, int max)
{
	if (in > max)
	{
		in = max;
	}
	else if (in < min)
	{
		in = min;
	}
	return in;
}

float map(float in, int posA, int posB, int pos1A, int pos1B)
{
	return (in - posA)/(posB-posA)*(pos1B-pos1A) + pos1A;
}
//PID Start
int P(int loc,int goal)
{
		error = (goal-loc);
	//	error = deadZone(error, -1,1);
	//	error = limit(error, -30, 30);

		return error*Kp;
}

float I()
{
		cumError = cumError + error;
		return (cumError)*Ki;
}

float D()
{
		return (error-oldError)*Kd;
}

float PID(int loc,int goal)
{
		return P(loc,goal) + I() + D();
}
//PID End
void setModuleValues()
{
	speed1 = (int)map(values[0][0], -1, 1, -127, 127);
	speed2 = (int)map(values[1][0], -1, 1, -127, 127);
	speed3 = (int)map(values[2][0], -1, 1, -127, 127);
	speed4 = (int)map(values[3][0], -1, 1, -127, 127);

	angle1 = (int)map(values[0][1], -180, 180, center1-180, center1+180);
	angle2 = (int)map(values[1][1], -180, 180, center2-180, center2+180);
	angle3 = (int)map(values[2][1], -180, 180, center3-180, center3+180);
	angle4 = (int)map(values[3][1], -180, 180, center4-180, center4+180);


}

void updateOdometry(){
	float wheelVectors[4][2]; //x and y movement of each wheel
	wheelVectors[0][0] = speed1*cosDegrees(sensorValue[enc1]+center1);
	wheelVectors[0][1] = speed1*sinDegrees(sensorValue[enc1]+center1);
	wheelVectors[1][0] = speed2*cosDegrees(sensorValue[enc2]+center2);
	wheelVectors[1][1] = speed2*sinDegrees(sensorValue[enc2]+center2);
	wheelVectors[2][0] = speed3*cosDegrees(sensorValue[enc3]+center3);
	wheelVectors[2][1] = speed3*sinDegrees(sensorValue[enc3]+center3);
	wheelVectors[3][0] = speed4*cosDegrees(sensorValue[enc4]+center4);
	wheelVectors[3][1] = speed4*sinDegrees(sensorValue[enc4]+center4);

	for(int fish=0; fish<4; fish++){
		botPos[0]+= wheelVectors[fish][0];
		botPos[1]+= wheelVectors[fish][1];
	}

}

void setAll()
{
		motor[wheel1]=speed1;
		motor[wheel2]=speed2;
		motor[wheel3]=speed3;
		motor[wheel4]=speed4;

		motor[swerve1]=(int)PID(SensorValue[enc1], angle1);
		motor[swerve2]=(int)PID(SensorValue[enc2], angle2);
		motor[swerve3]=(int)PID(SensorValue[enc3], angle3);
		motor[swerve4]=(int)PID(SensorValue[enc4], angle4);
}

//main
task main()
{
	resetAll();
	resetEnc();
	while(true)
	{
		calculateVectors(map(vexRT(Ch4),-127,127,-1,1),map(vexRT(Ch3),-127,127,-1,1),map(vexRT(Ch1),-127,127,-1,1));
		//calculateVectors(0.1,0,0);
		calculateWheelValues();
		setModuleValues();

		trim();

		setAll();

		updateOdometry();
		if (vexRT(Btn6D)) {
			resetEnc();
		}

		}
}