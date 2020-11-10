package com.firecode.test.concurrent.finall;

/**
 * 测试final能否被修改
 * 
 * final相关说明：
 * 1，一般被final修饰的变量它的值是不能变的，但是如果值是对象只是引用不能变，里面的属性是可以变的
 * 2，列外点被final修饰的引用变量，只要变量的类不是被final所修饰，该引用变量是可以通过反射修改的
 * 3，static修饰的函数不能被重写，但是子类可以有相同的函数（原因：静态函数其实可以看作是私有的）
 */
public class Final01TestFinal {

	String test;

	public static void main(String[] args) {
		final Final02Person person = new Final02Person();
		person.testFinal.test = "13g";
		System.out.println(person.testFinal.test);
		person.testFinal.test = "15g";
		System.out.println(person.testFinal.test);

//        String abc = "abc";
//        System.out.println(abc.substring(1, 2));
//        System.out.println(abc);
//        person.bag = "book";
//        person = new Person();
//        int age = person.age;
//        String name = person.name;
	}
}
