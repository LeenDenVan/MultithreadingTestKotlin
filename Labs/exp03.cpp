#include <iostream>
#include <iomanip>
#include <stdio.h>
#include <stdlib.h>
//#include <windows.h>
//#include <winbase.h>

#define queueSize 120			// ���пռ��С

using namespace std;

float queue[queueSize];			// ѭ������

int front, rear;				// ����ָ�룬��βָ�� 

void initiate()					// ��ʼ������ 
{	front = rear = 0;
} 

bool enqueue(float e)			// ��� 
{//************************************************
	queue[rear] = e;
	rear=(rear+1)%queueSize;
	return true;
 //================================================
}

float dequeue()					// ���� 
{//************************************************
	float res = queue[front];
	front = (front + 1) % queueSize;
	return res;
 //================================================
}
/*
void delay(int ms) 				// ��ʱ
{	int start = GetTickCount();
	while(GetTickCount()-start < ms);
} 
*/
int main()
{	int i, j;
	int group, NumPerGroup;		// �������������������ݸ��� 
	float data, average;		// ���ݣ�ĳ��ƽ��ֵ
	
	freopen("exp03.in", "r", stdin);
	freopen("exp03.out", "w", stdout);
	
	initiate();
	cin >> group;
	for(i=0; i<group; i++)
	{	for(j=0; j<100; j++)	// һ��ʱ�β����������
		{	cin >> data;
			if(data>=0.05 && data<=9.99)enqueue(data);
		}
		// delay(50);
		_sleep(50);						// ��ʱ��ģ��ʱ��ֶ� 
		average = 0.0;
	//************* �������ݳ��ӣ���ƽ�� *************
		enqueue(0);
		j=0;
		while( (data = dequeue() ) != 0 )average+=data,j++;
		average/=j;
	//================================================
		cout << setprecision(3) << setiosflags(ios::fixed | ios::showpoint) << average << ' ';
	}
	fclose(stdin);
	fclose(stdout);
	return 1;
}
