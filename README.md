# feign-parallel-classloader-bug

Sample app to reproduce a a classloader problem when using a load balanced (`spring-cloud-starter-loadbalancer`) `@FeignClient` in parallel threads (`stream.parallel()`)
when executing as a spring-boot bundled/executable `jar`.

Steps to reproduce the error:
1. `mvn spring-boot:run` should run without errors.
2. `mvn clean install && ./target/feign-parallel-classloader-bug-0.0.1-SNAPSHOT.jar` will fail with the following stacktrace:
    
    ```
    2021-08-04 13:18:34.560 ERROR 95826 --- [           main] o.s.boot.SpringApplication               : Application run failed

    java.lang.IllegalStateException: Failed to execute CommandLineRunner
            at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:794) ~[spring-boot-2.5.3.jar!/:2.5.3]
            at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:775) ~[spring-boot-2.5.3.jar!/:2.5.3]
            at org.springframework.boot.SpringApplication.run(SpringApplication.java:345) ~[spring-boot-2.5.3.jar!/:2.5.3]
            at org.springframework.boot.SpringApplication.run(SpringApplication.java:1343) ~[spring-boot-2.5.3.jar!/:2.5.3]
            at org.springframework.boot.SpringApplication.run(SpringApplication.java:1332) ~[spring-boot-2.5.3.jar!/:2.5.3]
            at es.didiez.feignparallelclassloaderbug.Application.main(Application.java:16) ~[classes!/:0.0.1-SNAPSHOT]
            at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
            at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
            at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
            at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
            at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:49) ~[feign-parallel-classloader-bug-0.0.1-SNAPSHOT.jar:0.0.1-SNAPSHOT]
            at org.springframework.boot.loader.Launcher.launch(Launcher.java:108) ~[feign-parallel-classloader-bug-0.0.1-SNAPSHOT.jar:0.0.1-SNAPSHOT]
            at org.springframework.boot.loader.Launcher.launch(Launcher.java:58) ~[feign-parallel-classloader-bug-0.0.1-SNAPSHOT.jar:0.0.1-SNAPSHOT]
            at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:88) ~[feign-parallel-classloader-bug-0.0.1-SNAPSHOT.jar:0.0.1-SNAPSHOT]
    Caused by: java.lang.IllegalArgumentException: java.lang.IllegalArgumentException: Could not find class [org.springframework.boot.autoconfigure.condition.OnPropertyCondition]
            at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:na]
            at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:na]
            at java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:na]
            at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:490) ~[na:na]
            at java.base/java.util.concurrent.ForkJoinTask.getThrowableException(ForkJoinTask.java:600) ~[na:na]
            at java.base/java.util.concurrent.ForkJoinTask.reportException(ForkJoinTask.java:678) ~[na:na]
            at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:737) ~[na:na]
            at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159) ~[na:na]
            at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173) ~[na:na]
            at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233) ~[na:na]
            at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497) ~[na:na]
            at es.didiez.feignparallelclassloaderbug.Application.lambda$run$1(Application.java:25) ~[classes!/:0.0.1-SNAPSHOT]
            at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:791) ~[spring-boot-2.5.3.jar!/:2.5.3]
            ... 13 common frames omitted
    Caused by: java.lang.IllegalArgumentException: Could not find class [org.springframework.boot.autoconfigure.condition.OnPropertyCondition]
            at org.springframework.util.ClassUtils.resolveClassName(ClassUtils.java:334) ~[spring-core-5.3.9.jar!/:5.3.9]
            at org.springframework.context.annotation.ConditionEvaluator.getCondition(ConditionEvaluator.java:124) ~[spring-context-5.3.9.jar!/:5.3.9]
            at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:96) ~[spring-context-5.3.9.jar!/:5.3.9]
            at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:88) ~[spring-context-5.3.9.jar!/:5.3.9]
            at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:71) ~[spring-context-5.3.9.jar!/:5.3.9]
            at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.doRegisterBean(AnnotatedBeanDefinitionReader.java:254) ~[spring-context-5.3.9.jar!/:5.3.9]
            at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.registerBean(AnnotatedBeanDefinitionReader.java:147) ~[spring-context-5.3.9.jar!/:5.3.9]
            at org.springframework.context.annotation.AnnotatedBeanDefinitionReader.register(AnnotatedBeanDefinitionReader.java:137) ~[spring-context-5.3.9.jar!/:5.3.9]
            at org.springframework.context.annotation.AnnotationConfigApplicationContext.register(AnnotationConfigApplicationContext.java:168) ~[spring-context-5.3.9.jar!/:5.3.9]
            at org.springframework.cloud.context.named.NamedContextFactory.createContext(NamedContextFactory.java:122) ~[spring-cloud-context-3.0.3.jar!/:3.0.3]
            at org.springframework.cloud.context.named.NamedContextFactory.getContext(NamedContextFactory.java:101) ~[spring-cloud-context-3.0.3.jar!/:3.0.3]
            at org.springframework.cloud.context.named.NamedContextFactory.getInstances(NamedContextFactory.java:181) ~[spring-cloud-context-3.0.3.jar!/:3.0.3]
            at org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient.execute(FeignBlockingLoadBalancerClient.java:85) ~[spring-cloud-openfeign-core-3.0.3.jar!/:3.0.3]
            at feign.SynchronousMethodHandler.executeAndDecode(SynchronousMethodHandler.java:119) ~[feign-core-10.12.jar!/:na]
            at feign.SynchronousMethodHandler.invoke(SynchronousMethodHandler.java:89) ~[feign-core-10.12.jar!/:na]
            at feign.ReflectiveFeign$FeignInvocationHandler.invoke(ReflectiveFeign.java:100) ~[feign-core-10.12.jar!/:na]
            at es.didiez.feignparallelclassloaderbug.$Proxy60.getAlbum(Unknown Source) ~[na:0.0.1-SNAPSHOT]
            at es.didiez.feignparallelclassloaderbug.Application.lambda$run$0(Application.java:24) ~[classes!/:0.0.1-SNAPSHOT]
            at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195) ~[na:na]
            at java.base/java.util.stream.LongPipeline$1$1.accept(LongPipeline.java:177) ~[na:na]
            at java.base/java.util.stream.Streams$RangeLongSpliterator.forEachRemaining(Streams.java:228) ~[na:na]
            at java.base/java.util.Spliterator$OfLong.forEachRemaining(Spliterator.java:763) ~[na:na]
            at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484) ~[na:na]
            at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290) ~[na:na]
            at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746) ~[na:na]
            at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290) ~[na:na]
            at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1020) ~[na:na]
            at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1656) ~[na:na]
            at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1594) ~[na:na]
            at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:183) ~[na:na]
    Caused by: java.lang.ClassNotFoundException: org.springframework.boot.autoconfigure.condition.OnPropertyCondition
            at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581) ~[na:na]
            at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178) ~[na:na]
            at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:522) ~[na:na]
            at java.base/java.lang.Class.forName0(Native Method) ~[na:na]
            at java.base/java.lang.Class.forName(Class.java:398) ~[na:na]
            at org.springframework.util.ClassUtils.forName(ClassUtils.java:284) ~[spring-core-5.3.9.jar!/:5.3.9]
            at org.springframework.util.ClassUtils.resolveClassName(ClassUtils.java:324) ~[spring-core-5.3.9.jar!/:5.3.9]
            ... 29 common frames omitted
    ```
