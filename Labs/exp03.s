	.file	"exp03.cpp"
	.text
	.type	_Z41__static_initialization_and_destruction_0ii, @function
_Z41__static_initialization_and_destruction_0ii:
.LFB2687:
	.cfi_startproc
	cmpl	$1, %edi
	je	.L7
.L4:
	ret
.L7:
	cmpl	$65535, %esi
	jne	.L4
	subq	$8, %rsp
	.cfi_def_cfa_offset 16
	leaq	_ZStL8__ioinit(%rip), %rdi
	call	_ZNSt8ios_base4InitC1Ev@PLT
	leaq	__dso_handle(%rip), %rdx
	leaq	_ZStL8__ioinit(%rip), %rsi
	movq	_ZNSt8ios_base4InitD1Ev@GOTPCREL(%rip), %rdi
	call	__cxa_atexit@PLT
	addq	$8, %rsp
	.cfi_def_cfa_offset 8
	ret
	.cfi_endproc
.LFE2687:
	.size	_Z41__static_initialization_and_destruction_0ii, .-_Z41__static_initialization_and_destruction_0ii
	.globl	_Z8initiatev
	.type	_Z8initiatev, @function
_Z8initiatev:
.LFB2185:
	.cfi_startproc
	endbr64
	movl	$0, rear(%rip)
	movl	$0, front(%rip)
	ret
	.cfi_endproc
.LFE2185:
	.size	_Z8initiatev, .-_Z8initiatev
	.globl	_Z7enqueuef
	.type	_Z7enqueuef, @function
_Z7enqueuef:
.LFB2186:
	.cfi_startproc
	endbr64
	movl	rear(%rip), %edx
	movslq	%edx, %rcx
	leaq	queue(%rip), %rax
	movss	%xmm0, (%rax,%rcx,4)
	addl	$1, %edx
	movslq	%edx, %rax
	imulq	$-2004318071, %rax, %rax
	shrq	$32, %rax
	addl	%edx, %eax
	sarl	$6, %eax
	movl	%edx, %ecx
	sarl	$31, %ecx
	subl	%ecx, %eax
	imull	$120, %eax, %eax
	subl	%eax, %edx
	movl	%edx, rear(%rip)
	movl	$1, %eax
	ret
	.cfi_endproc
.LFE2186:
	.size	_Z7enqueuef, .-_Z7enqueuef
	.globl	_Z7dequeuev
	.type	_Z7dequeuev, @function
_Z7dequeuev:
.LFB2187:
	.cfi_startproc
	endbr64
	movl	front(%rip), %edx
	movslq	%edx, %rcx
	leaq	queue(%rip), %rax
	movss	(%rax,%rcx,4), %xmm0
	addl	$1, %edx
	movslq	%edx, %rax
	imulq	$-2004318071, %rax, %rax
	shrq	$32, %rax
	addl	%edx, %eax
	sarl	$6, %eax
	movl	%edx, %ecx
	sarl	$31, %ecx
	subl	%ecx, %eax
	imull	$120, %eax, %eax
	subl	%eax, %edx
	movl	%edx, front(%rip)
	ret
	.cfi_endproc
.LFE2187:
	.size	_Z7dequeuev, .-_Z7dequeuev
	.section	.rodata.str1.1,"aMS",@progbits,1
.LC1:
	.string	"r"
.LC2:
	.string	"exp03.in"
	.text
	.globl	main
	.type	main, @function
main:
.LFB2188:
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	pushq	%rbx
	.cfi_def_cfa_offset 24
	.cfi_offset 3, -24
	subq	$56, %rsp
	.cfi_def_cfa_offset 80
	movq	%fs:40, %rax
	movq	%rax, 40(%rsp)
	xorl	%eax, %eax
	movq	stdin(%rip), %rdx
	leaq	.LC1(%rip), %rsi
	leaq	.LC2(%rip), %rdi
	call	freopen@PLT
	call	_Z8initiatev
	leaq	32(%rsp), %rsi
	leaq	_ZSt3cin(%rip), %rdi
	call	_ZNSirsERi@PLT
	movl	$0, %ebp
	jmp	.L21
.L28:
	movaps	%xmm1, %xmm0
	call	_Z7enqueuef
.L14:
	addl	$1, %ebx
.L17:
	cmpl	$99, %ebx
	jg	.L13
	leaq	36(%rsp), %rsi
	leaq	_ZSt3cin(%rip), %rdi
	call	_ZNSi10_M_extractIfEERSiRT_@PLT
	movss	36(%rsp), %xmm1
	pxor	%xmm0, %xmm0
	cvtss2sd	%xmm1, %xmm0
	comisd	.LC3(%rip), %xmm0
	jb	.L14
	movsd	.LC4(%rip), %xmm2
	comisd	%xmm0, %xmm2
	jb	.L14
	jmp	.L28
.L13:
	pxor	%xmm0, %xmm0
	call	_Z7enqueuef
#APP
# 64 "exp03.cpp" 1
	movq %rax, %rsi
# 0 "" 2
#NO_APP
	pxor	%xmm5, %xmm5
	movss	%xmm5, 12(%rsp)
	movl	$0, %ebx
	jmp	.L20
.L25:
	addss	12(%rsp), %xmm0
	movss	%xmm0, 12(%rsp)
	addl	$1, %ebx
.L20:
	call	_Z7dequeuev
	movss	%xmm0, 36(%rsp)
	ucomiss	.LC0(%rip), %xmm0
	jp	.L25
	jne	.L25
	pxor	%xmm0, %xmm0
	cvtsi2ssl	%ebx, %xmm0
	movss	12(%rsp), %xmm4
	divss	%xmm0, %xmm4
	leaq	_ZSt4cout(%rip), %rdi
	movq	_ZSt4cout(%rip), %rax
	movq	%rdi, %rdx
	addq	-24(%rax), %rdx
	movq	$3, 8(%rdx)
	movq	%rdi, %rdx
	addq	-24(%rax), %rdx
	movl	24(%rdx), %eax
	orl	$1028, %eax
	movl	%eax, 24(%rdx)
	pxor	%xmm0, %xmm0
	cvtss2sd	%xmm4, %xmm0
	call	_ZNSo9_M_insertIdEERSoT_@PLT
	movq	%rax, %rdi
	movb	$32, 31(%rsp)
	leaq	31(%rsp), %rsi
	movl	$1, %edx
	call	_ZSt16__ostream_insertIcSt11char_traitsIcEERSt13basic_ostreamIT_T0_ES6_PKS3_l@PLT
	addl	$1, %ebp
.L21:
	cmpl	%ebp, 32(%rsp)
	jle	.L12
	movl	$0, %ebx
	jmp	.L17
.L12:
	movq	stdin(%rip), %rdi
	call	fclose@PLT
	movq	stdout(%rip), %rdi
	call	fclose@PLT
	movq	40(%rsp), %rax
	xorq	%fs:40, %rax
	jne	.L29
	movl	$1, %eax
	addq	$56, %rsp
	.cfi_remember_state
	.cfi_def_cfa_offset 24
	popq	%rbx
	.cfi_def_cfa_offset 16
	popq	%rbp
	.cfi_def_cfa_offset 8
	ret
.L29:
	.cfi_restore_state
	call	__stack_chk_fail@PLT
	.cfi_endproc
.LFE2188:
	.size	main, .-main
	.type	_GLOBAL__sub_I_queue, @function
_GLOBAL__sub_I_queue:
.LFB2688:
	.cfi_startproc
	endbr64
	subq	$8, %rsp
	.cfi_def_cfa_offset 16
	movl	$65535, %esi
	movl	$1, %edi
	call	_Z41__static_initialization_and_destruction_0ii
	addq	$8, %rsp
	.cfi_def_cfa_offset 8
	ret
	.cfi_endproc
.LFE2688:
	.size	_GLOBAL__sub_I_queue, .-_GLOBAL__sub_I_queue
	.section	.init_array,"aw"
	.align 8
	.quad	_GLOBAL__sub_I_queue
	.globl	rear
	.bss
	.align 4
	.type	rear, @object
	.size	rear, 4
rear:
	.zero	4
	.globl	front
	.align 4
	.type	front, @object
	.size	front, 4
front:
	.zero	4
	.globl	queue
	.align 32
	.type	queue, @object
	.size	queue, 480
queue:
	.zero	480
	.local	_ZStL8__ioinit
	.comm	_ZStL8__ioinit,1,1
	.section	.rodata.cst4,"aM",@progbits,4
	.align 4
.LC0:
	.long	0
	.section	.rodata.cst8,"aM",@progbits,8
	.align 8
.LC3:
	.long	2576980378
	.long	1068079513
	.align 8
.LC4:
	.long	1202590843
	.long	1076099809
	.hidden	__dso_handle
	.ident	"GCC: (Ubuntu 9.3.0-17ubuntu1~20.04) 9.3.0"
	.section	.note.GNU-stack,"",@progbits
	.section	.note.gnu.property,"a"
	.align 8
	.long	 1f - 0f
	.long	 4f - 1f
	.long	 5
0:
	.string	 "GNU"
1:
	.align 8
	.long	 0xc0000002
	.long	 3f - 2f
2:
	.long	 0x3
3:
	.align 8
4:
