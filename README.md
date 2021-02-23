# MyPlugins
my plugins for minecraft spigot server  
这是一个用来放置我为我的minecraft服务器编写的插件的代码仓库  
**EnhanceFrameWork**是一个附魔框架，example是一个它的应用例子 
**EnhanceFrameWork**单独打包没有任何意义，spigot插件加载器也不会加载它
你需要将其作为依赖项一并打包

**Recipes**包含了一些额外的合成表和合成物品的效果，它现在可以改为使用EhanceFrameWork实现，但我还没这样做，因为这会使服务器原有的相关物品不好处理  
不过其中也有部分物品使用了附魔框架。  
**WizardStaff**是使用附魔框架制作的一个主动施法型附魔包  

# EnhanceFrameWork附魔框架
本框架的主要意义在于帮助你省略自定义附魔时繁多的装备属性检测  
另外也可以用于可充能法杖类物品  
对于已经在代码中声明的法术（框架中没有已经声明的法术/附魔，但你可以下载release中的WizardStaff或是example）  
在物品的Lore中加入一行，内容为法术/附魔名即可生效    
如果希望有使用次数限制，可以在后面追加<剩余次数/最大次数>    
例如：    
```FIRE_BALL<10/10>```    
使用本框架：  
你可以直接使用github提供的raw下载服务来下载这个包  
如果你使用maven，你需要先添加仓库：  
 ```
    <repositories>
        <repository>
            <id>github-navymaster-repo</id>
            <url>https://raw.githubusercontent.com/navymaster/MyPlugins/master/maven-repo</url>
        </repository>
    </repositories> 
 ```
 然后添加本框架到依赖：
 ```
     <dependencies>
        <dependency>
            <groupId>cn.navy_master</groupId>
            <artifactId>EnhanceFrameWork</artifactId>
            <version>1.1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
 ```
 另外，你可以在这里查看javadoc：
 https://navymaster.github.io/MyPlugins/docs/
 

  
