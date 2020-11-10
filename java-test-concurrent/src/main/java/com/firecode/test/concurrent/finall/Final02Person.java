package com.firecode.test.concurrent.finall;

/**
 * 不可变的对象，演示其他类无法修改这个对象，public也不行
 * 
 * 注意：
 * 1，一般被final修饰的变量它的值是不能变的，但是如果值是对象只是引用不能变，里面的属性是可以变的
 * 2，列外点被final修饰的引用变量，只要变量的类不是被final所修饰，该引用变量是可以通过反射修改的
 * 3，static修饰的函数不能被重写，但是子类可以有相同的函数（原因：静态函数其实可以看作是私有的）
 */
public class Final02Person {

	final int age = 18;
	String alice = new String("Alice");
	final String name = alice;
	final Final01TestFinal testFinal = new Final01TestFinal();

	public static void main(String[] args) {
		Final02Person person = new Final02Person();
		person.alice = "44";
		System.out.println(person.name);
	}
}
