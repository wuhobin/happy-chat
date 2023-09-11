package com.wuhobin.rest.redisson;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/9/11 12:15
 * @description
 */
public interface RedissonLockService {


    /**
     * 用来尝试获取锁，如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false .
     * 如果此时其他线程已经获取到锁，另外一个线程尝试获取锁直接返回失败
     * 加锁，锁默认的过期时间为30s
     * @param key hash结构的key
     * @return 是否获取到锁
     */
    boolean tryLock(String key);


    /**
     *  tryLock(long time, TimeUnit unit)方法和tryLock()方法是类似的，只不过区别在于这个方法在拿不到锁时会等待一定的时间，
     *  在时间期限之内如果还拿不到锁，就返回false。如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。
     * 加锁，锁默认过期时间为30s
     * @param waitTime 超时等待时间
     * @param key hash结构的key
     * @return 是否获取到锁
     * @throws InterruptedException
     */
    boolean lock(long waitTime, String key) throws InterruptedException;



    /**
     * 默认超时等待时间为5s
     * time 在时间期限之内如果还拿不到锁，就返回false。如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。
     * 加锁，锁默认过期时间为30s
     * @param key hash结构的key
     * @return 是否获取到锁
     * @throws InterruptedException
     */
    boolean lock(String key) throws InterruptedException;


    /**
     * 如果使用此方法，redisson自动续约的机制就不会生效
     * 多添加一个锁的有效时间
     * @param waitTime 等待时间，在等待时间内未获取到锁直接返货false
     * @param leaseTime 锁的有效时间，锁会在有效时间之后自动释放，这里需要注意，执行业务代码的时间要小于锁的有效时间
     * @param key hash结构的key
     * @return 是否获取到锁
     * @throws InterruptedException
     */
    boolean lock(long waitTime, long leaseTime, String key) throws InterruptedException;




    /**
     * 解锁
     * @param key hash结构的key
     */
    void unLock(String key);


}
