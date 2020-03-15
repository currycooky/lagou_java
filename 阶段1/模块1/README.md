1. Mybatis动态sql是做什么的？都有哪些动态sql？简述一下动态sql的执行原理？

    > MyBatis动态sql是通过xml映射文件，动态的编写sql，完成逻辑判断和动态拼接sql的功能。
    >
    > 常用的动态sql有：`if`、`where`、`sql`、`when`、`trim`、`foreach`、`set`、`choose`、`otherwise`等等。
    >
    > 其执行原理为：使用**OGNL**（Object Graphic Navigation Language对象图导航语言）表达式，从 sql 参数对象中计算表达式的值，根据表达式的值动态拼接sql，以此来完成动态sql的功能。

2. Mybatis是否支持延迟加载？如果支持，它的实现原理是什么？

    > Mybatis仅支持association关联对象和collection关联集合对象的延迟加载，association指的就是一对一，collection指的就是一对多查询。在Mybatis配置文件中，可以配置是否启用延迟加载`lazyLoadingEnabled`。
    >
    > 它的原理是：使用CGLIB创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，比如调用`a.getB().getName()`，拦截器`invoke()`方法发现`a.getB()`是null值，那么就会单独发送事先保存好的查询关联B对象的sql，把B查询上来，然后调用`a.setB(b)`，于是a的对象b属性就有值了，接着完成`a.getB().getName()`方法的调用。这就是延迟加载的基本原理。

3. Mybatis都有哪些Executor执行器？它们之间的区别是什么？

    > Mybatis有三种基本的Executor执行器，`SimpleExecutor`、`ReuseExecutor`、`BatchExecutor`。
    >
    > * SimpleExecutor：每执行一次update或select，就开启一个Statement对象，用完立刻关闭Statement对象。
    >
    > * ReuseExecutor：执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，而是放置于**Map**内，供下一次使用。简言之，就是重复使用Statement对象。
    >
    > * BatchExecutor：执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（`addBatch()`），等待统一执行（`executeBatch()`），它缓存了多个Statement对象，每个Statement对象都是`addBatch()`完毕后，等待逐一执行`executeBatch()`批处理。与JDBC批处理相同。
    > 
    > 作用范围：Executor的这些特点，都严格限制在SqlSession生命周期范围内。

4. 简述下Mybatis的一级、二级缓存（分别从存储结构、范围、失效场景。三个方面来作答）？

    > 一级缓存：基于SqlSession级别的，也就是说某个SqlSession进行某个查询操作后会将该结果暂时缓存起来，而后在所有的SqlSession没有对该表进行插入、修改、删除操作的情况下，当这个SqlSession再次发起此查询时SqlSession不会去数据库执行查询操作，而是直接从缓存拿出上次查询的结果。不同的SqlSession之间缓存的数据互不影响。一级缓存是使用HashMap直接存储的对象。
    >
    > 二级缓存：是基于Mapper级别的，也就是说多个SqlSession去使用某个Mapper的查询语句时，得到的缓存数据是可共用的。二级缓存是存储的数据，而非对象。

5. 简述Mybatis的插件运行原理，以及如何编写一个插件？

    > 运行原理：编写针对Executor、StatementHandler、ParameterHandler、ResultSetHandler四个接口的插件，MyBatis使用JDK的动态代理为需要拦截的接口生成代理对象，然后实现接口的拦截方法，所以当执行需要拦截的接口方法时，会进入拦截方法。
    >
    > 编写插件步骤：
    >
    > * 编写Intercepror接口的实现类；
    > * 设置插件的签名，告诉MyBatis拦截具体哪个类的哪个方法；
    > * 将插件注册到配置文件中。
