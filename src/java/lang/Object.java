/*
 * @(#)Object.java	1.32 95/12/03  
 *
 * Copyright (c) 1994 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies. Please refer to the file "copyright.html"
 * for further important copyright and licensing information.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package java.lang;

/**
 * 类层次结构的根。系统中的每个类都有Object作为其最终父类。
 * 这里定义的每个变量和方法在每个对象中都可用。
 * 
 * Object类是Java类层次结构的根基，所有的Java类都直接或间接地继承自Object类。
 * 即使没有显式地声明extends Object，编译器也会自动为每个类添加这一继承关系。
 * 因此，Object类中定义的方法对所有Java对象都是可用的。
 * 
 * Object类提供了一系列基础方法，如获取对象的类信息、比较对象相等性、创建对象的副本、
 * 获取对象的字符串表示形式以及线程同步相关的方法等。这些方法构成了Java对象模型的基础。
 * 
 * @see		Class
 * @version 	1.32, 12/03/95
 */
public class Object {
    /**
     * 返回此Object的Class。Java对于类有运行时表示形式——Class类型的描述符——
     * getClass()方法可以为任何Object返回此描述符。
     * 
     * 此方法返回表示对象运行时类的Class对象。返回的Class对象是由这个对象的类的静态类型所定义的类，
     * 或者是接口的实现类。例如，如果对象obj是String类的实例，则obj.getClass()将返回String.class。
     * 
     * Class对象包含了关于类的元数据信息，如类名、超类、实现的接口、字段和方法等。可以通过Class对象
     * 来进行反射操作，获取类的结构信息或动态创建对象。
     * 
     * 注意：此方法是native和final的，意味着它是由底层JVM实现的，并且不能被子类重写。
     * 
     * @return 表示此对象运行时类的Class对象
     */
    public final native Class getClass();

    /**
     * 返回此Object的哈希码。
     * Java系统中的每个Object都有一个哈希码。哈希码是一个通常对不同对象不同的数字。
     * 它用于将对象存储在哈希表中。
     * 注意：哈希码可以是负数也可以是正数。
     * 
     * 哈希码是一个整数值，通常用于快速比较对象。虽然不是绝对要求，但通常相等的对象应该产生相同的哈希码。
     * 此方法通常在Java中的集合类中使用，如HashMap、HashSet等，用于确定对象在内部数据结构中的存储位置。
     * 
     * 默认的哈希码实现通常基于对象的内存地址，但子类可以重写此方法以提供更适合特定类的实现。
     * 例如，String类的哈希码基于字符串的内容而不是对象的地址。
     * 
     * 当重写equals方法时，通常也应该重写hashCode方法，以保持"相等的对象具有相同的哈希码"的约定。
     * 
     * @see		java.util.Hashtable
     * @return 此对象的哈希码值
     */
    public native int hashCode();

    /**
     * 比较两个Object是否相等。
     * 返回一个布尔值，表示此对象是否等同于指定的对象。当对象存储在哈希表中时使用此方法。
     * 
     * 默认实现使用"=="运算符比较对象引用，即它检查两个引用是否指向内存中的同一个对象。
     * 这种比较称为"引用相等性"或"身份比较"。
     * 
     * 许多类重写此方法以提供"逻辑相等性"比较，即基于对象的状态或内容进行比较，
     * 而不仅仅是比较它们的引用。例如，String类重写了equals方法来比较字符串的内容。
     * 
     * 重写equals方法时，应遵循以下规则：
     * 1. 自反性：对于任何非空引用x，x.equals(x)应返回true
     * 2. 对称性：对于任何非空引用x和y，如果x.equals(y)返回true，那么y.equals(x)也应返回true
     * 3. 传递性：对于任何非空引用x、y和z，如果x.equals(y)返回true且y.equals(z)返回true，
     *    那么x.equals(z)也应返回true
     * 4. 一致性：对于任何非空引用x和y，多次调用x.equals(y)应该始终返回相同的结果
     * 5. 对于任何非空引用x，x.equals(null)应返回false
     * 
     * @param	obj	要与之比较的对象
     * @return	如果这些对象相等则为true；否则为false。
     * @see		java.util.Hashtable
     */
    public boolean equals(Object obj) {
	return (this == obj);
    }

    /**
     * 创建对象的克隆。分配一个新实例，并将当前对象的按位克隆放入新对象中。
     * 
     * clone方法创建并返回此对象的一个副本。"副本"的确切含义取决于对象的类。
     * 一般而言，对于任何对象x，表达式x.clone() != x将为true，并且
     * x.clone().getClass() == x.getClass()也为true，但这些不是绝对的要求。
     * 
     * 默认的clone实现执行"浅拷贝"——它只复制对象的字段值，而不会递归复制引用字段指向的对象。
     * 如果一个对象包含对其他对象的引用，那么这些引用将在克隆中保持不变，指向与原始对象相同的对象。
     * 
     * 要使一个类支持克隆，它必须：
     * 1. 实现Cloneable接口（这是一个标记接口，没有方法）
     * 2. 重写Object.clone()方法（通常调用super.clone()）
     * 
     * 如果一个类需要"深拷贝"（复制引用字段指向的对象），它必须在其clone方法中显式地执行这种复制。
     * 
     * @return		此对象的克隆。
     * @exception	OutOfMemoryError 如果没有足够的内存。
     * @exception	CloneNotSupportedException 对象明确不想被克隆，或者它不支持Cloneable接口。
     */
    protected native Object clone() throws CloneNotSupportedException;

    /**
     * 返回表示此对象值的String。建议所有子类都重写此方法。
     * 
     * toString方法返回对象的字符串表示。此方法的目的是返回一个简洁但信息丰富的字符串，
     * 易于人类阅读。建议所有子类都重写此方法。
     * 
     * 默认实现返回一个字符串，由对象的类名后跟"@"符号和对象哈希码的无符号十六进制表示组成。
     * 例如："java.lang.Object@7d9d1a"。这通常不是很有用，因此大多数类都会提供自己的实现。
     * 
     * toString方法在以下情况下自动调用：
     * 1. 当对象被传递给System.out.println()等打印方法时
     * 2. 当对象与字符串连接时（使用"+"运算符）
     * 3. 当String.valueOf(obj)被调用时
     * 
     * @return 表示此对象的字符串
     */
    public String toString() {
	return getClass().getName() + "@" + 
                     Integer.toString(hashCode() << 1 >>> 1, 16);
    }

    /**
     * 在另一个线程的条件发生变化时通知单个等待线程。
     * 影响变化的线程使用notify()通知等待线程。想要等待条件变化才继续的线程可以调用wait()。
     * 
     * notify方法用于线程间通信，通知那些在当前对象上调用了wait方法而被阻塞的线程。
     * 当线程调用notify时，JVM会选择一个正在该对象的等待集上等待的线程，并将其唤醒。
     * 被唤醒的线程将从wait调用返回，并尝试重新获取对象的锁。
     * 
     * <em>notify()方法只能从同步方法内部调用。</em>这意味着调用线程必须是当前对象监视器的所有者，
     * 也就是说，它必须持有对象的锁。如果线程不拥有对象的锁，将抛出IllegalMonitorStateException。
     * 
     * notify方法是线程协作的基础机制之一，常用于实现生产者-消费者模式等并发编程模式。
     *
     * @exception	IllegalMonitorStateException 如果当前线程不是对象监视器的所有者。
     * @see		Object#wait
     * @see		Object#notifyAll
     */
    public final native void notify() throws IllegalMonitorStateException;

    /**
     * 通知所有等待条件变化的线程。
     * 通常，等待的线程是在等待另一个线程改变某些条件。因此，影响多个线程等待的条件变化的线程
     * 使用notifyAll()方法通知所有等待的线程。想要等待条件变化才继续的线程可以调用wait()。
     * 
     * notifyAll方法唤醒所有在该对象的等待集上等待的线程。这些线程从wait调用中返回后，
     * 需要重新获取对象的锁，因此它们将相互竞争。
     * 
     * 与notify相比，notifyAll更可靠，特别是当多个条件变量共享同一个锁时。
     * 使用notifyAll可以避免"信号丢失"问题，确保所有等待的线程都有机会检查其条件。
     * 
     * <em>notifyAll()方法只能从同步方法内部调用。</em>这意味着调用线程必须是当前对象监视器的所有者。
     *
     * @exception	IllegalMonitorStateException 如果当前线程不是对象监视器的所有者。
     * @see		Object#wait
     * @see		Object#notify
     */
    public final native void notifyAll() throws IllegalMonitorStateException;

    /**
     * 导致线程等待，直到它被通知或指定的超时时间过去。
     * 
     * wait方法导致当前线程放弃对象上的锁并等待，直到另一个线程调用相同对象上的notify或notifyAll方法，
     * 或者指定的时间已过。然后线程将被唤醒，并尝试重新获取对象的锁。
     * 
     * 当线程调用wait时，它会释放对象的锁，并进入对象的等待集。释放锁允许其他线程获取该锁并执行
     * 同步方法或代码块。当线程被唤醒后，它必须重新获取对象的锁才能继续执行。
     * 
     * <em>wait()方法只能从同步方法内部调用。</em>
     *
     * @param timeout	等待的最长时间（以毫秒为单位）
     * @exception	IllegalMonitorStateException 如果当前线程不是对象监视器的所有者。
     * @exception 	InterruptedException 另一个线程中断了此线程。
     */
    public final native void wait(long timeout) throws InterruptedException, IllegalMonitorStateException;

    /**
     * 更精确的wait。
     * 
     * 此方法允许更精确的超时控制，可以指定额外的纳秒级延迟。
     * 内部实现会根据纳秒参数值，适当调整毫秒参数，然后调用基本的wait(long)方法。
     * 
     * <em>wait()方法只能从同步方法内部调用。</em>
     *
     * @param timeout	等待的最长时间（以毫秒为单位）
     * @param nano      额外的时间，以纳秒为单位，范围0-999999
     * @exception	IllegalMonitorStateException 如果当前线程不是对象监视器的所有者。
     * @exception 	InterruptedException 另一个线程中断了此线程。
     */
    public final void wait(long timeout, int nanos) throws InterruptedException, IllegalMonitorStateException {
	if (nanos >= 500000 || (nanos != 0 && timeout==0))
	    timeout++;
	wait(timeout);
    }

    /**
     * 导致线程永远等待，直到它被通知。
     * 
     * 此方法相当于调用wait(0)，表示线程将无限期等待，直到被显式地通知。
     * 这是一种常见的同步模式，用于实现条件变量，线程等待某个条件满足时继续执行。
     * 
     * <em>wait()方法只能从同步方法内部调用</em>
     *
     * @exception	IllegalMonitorStateException 如果当前线程不是对象监视器的所有者。
     * @exception 	InterruptedException 另一个线程中断了此线程。
     */
    public final void wait() throws InterruptedException, IllegalMonitorStateException {
	wait(0);
    }

    /**
     * 当此对象被垃圾回收时要执行的代码。
     * 默认情况下，不需要执行任何操作。
     * 
     * finalize方法是Java对象生命周期中的最后一个阶段。当垃圾收集器确定再没有对对象的引用时，
     * 它会在实际回收对象占用的内存之前调用此方法。
     * 
     * 此方法通常用于释放非Java资源（如文件句柄、网络连接等），这些资源不受Java自动内存管理的控制。
     * 
     * 由finalize方法抛出的任何异常都会导致终止终结过程，但否则它会被忽略。
     * 
     * 注意：依赖finalize进行清理是危险的，因为没有保证finalize方法会被及时调用，甚至不保证它会被调用。
     * 现代Java编程实践通常建议使用try-with-resources结构或显式的close方法，而不是依赖finalize。
     */
    protected void finalize() throws Throwable { }
}
