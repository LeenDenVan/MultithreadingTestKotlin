#include <iostream>
#include <iomanip>
#include <stdio.h>
#include <stdlib.h>
//#include <windows.h>
//#include <winbase.h>

#define queueSize 120			// 队列空间大小

using namespace std;

float queue[queueSize];			// 循环队列

int front, rear;				// 队首指针，队尾指针 

void initiate()					// 初始化队列 
{	front = rear = 0;
} 

bool enqueue(float e)			// 入队 
{//************************************************
	queue[rear] = e;
	rear=(rear+1)%queueSize;
	return true;
 //================================================
}

float dequeue()					// 出队 
{//************************************************
	float res = queue[front];
	front = (front + 1) % queueSize;
	return res;
 //================================================
}
/*
void delay(int ms) 				// 延时
{	int start = GetTickCount();
	while(GetTickCount()-start < ms);
} 
*/
int main()
{	int i, j;
	int group, NumPerGroup;		// 采样分组数，出队数据个数 
	float data, average;		// 数据，某组平均值
	
	freopen("exp03.in", "r", stdin);
	freopen("exp03.out", "w", stdout);
	
	initiate();
	cin >> group;
	for(i=0; i<group; i++)
	{	for(j=0; j<100; j++)	// 一个时段采样数据入队
		{	cin >> data;
			if(data>=0.05 && data<=9.99)enqueue(data);
		}
		// delay(50);
		_sleep(50);						// 延时，模拟时间分段 
		average = 0.0;
	//************* 采样数据出队，求平均 *************
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
