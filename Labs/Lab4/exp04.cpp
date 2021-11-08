#include <iostream>
#include <stdio.h>

#define M 64 
using namespace std;

typedef
	struct
	{	int i, j, v;	// ��ʾ�С��С�ֵ 
	}triple;	// ��Ԫ������ 

typedef
	struct
	{	triple data[M];
		int row, col, num;
	}triGroup;	// ��Ԫ������� 

void input(triGroup &A)	// �������ݽ�����Ԫ��� 
{//************************************************
	cin>>A.row>>A.col>>A.num;
	int i = 0;
	for(; i < A.row; i++){
		cin>>A.data[i].i>>A.data[i].j>>A.data[i].v;
	}
 //================================================
}

void output(const triGroup &A)	// �����Ԫ��� 
{	int p;
	cout<<A.row<<' '<<A.col<<' '<<A.num<<endl;
	for(p=0; p<A.num; p++)
		cout<<A.data[p].i<<' '<<A.data[p].j<<' '<<A.data[p].v<<endl;
}

void transSM(const triGroup &A, triGroup &B)	// ת�þ���
{//************************************************
	B.row = A.col, B.col = A.row, B.num = A.num;
	triple mid;
	int i = 0, j = 0, min_i = 0;
	for(; i < A.num; i++){
		B.data[i].i = A.data[i].j;
		B.data[i].j = A.data[i].i;
		B.data[i].v = A.data[i].v;
	}
	for(i = 0; i < A.num-1; i++){
		min_i = i;
		for(j = i + 1; j < A.num; j++){
			if(B.data[min_i].i > B.data[j].i || (B.data[min_i].i == B.data[j].i && B.data[min_i].j > B.data[j].j)){
				min_i = j;
			}
		}
		mid = *&B.data[min_i];
		*&B.data[min_i] = *&B.data[i];
		*&B.data[i] = mid;
	}
 //================================================
}


int main()
{	triGroup S, T;
	freopen("exp04.in", "r", stdin);
	freopen("exp04.out", "w", stdout);
	input(S);
	transSM(S,T);
	output(T);
	fclose(stdin);
	fclose(stdout);
	return 1;
}
