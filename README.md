# Camel Jbang チュートリアル

## 前提条件

* JBangがインストールされていること
* VSCodeまたはVSCodeのフォークOSSをインストールしていること

## `camel`コマンドのインストール

以下のコマンドでcamelコマンドをインストールできる。

```sh
jbang app install camel@apache/camel
```

Note: Linux, Unixの場合には、camelが`~/.jbang/bin`等にインストールされ、PATHを通す設定が`.bashrc`や`.zshrc`に追記されるので、必要に応じてrcファイルを評価するか、ターミナルを立ち上げ直す。

以下のコマンドで正しくインストールできているかとcamelのバージョンを確認する。

```sh
camel version
```

## VSCodeの設定

拡張機能検索欄に`@recommended`を入力し、表示される拡張機能をインストールし、有効化する。

## Helloをプリントするルートの作成

### お手本

以下のフォルダのプログラム

* Javaでのルート実装: hello-from-java
* XMLでのルート実装: hello-from-xml
* YAMLでのルート実装: hello-from-yaml
* Groovyでのルート実装: hello-from-groovy
* 複数のファイルでのルート実装: double-hello
* プレースホルダーを用いるルート実装: hello-you
* Javaでのquickなルート実装: hello-quick

### Javaでのルート実装

以下のコマンドでサンプルのjavaでのルート実装を用意できる。

```
camel init hello.java
```

ここで、`hello.java`を任意のディレクトリに配置したい場合は、`--directory`オプションを用いる。

例えば、`hoge`ディレクトリに`hello.java`を用意するには、以下のコマンドを実行する。

```
camel init hello.java --directory hoge
```

中身は以下のファイルのようになっている。

```java
import org.apache.camel.builder.RouteBuilder;

public class hello extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:java?period=1000")
            .setBody()
                .simple("Hello Camel from ${routeId}")
            .log("${body}");
    }
}
```

`org.apache.camel.builder.RouteBuilder`を継承する形でクラスを実装し、その中の`configure()`をオーバーライドする形でルートを定義していく。

`from`で指定しているエントリーポイントは、別のルートのエンドポイント。

`setBody`や`simple`、`log`はすべてコンポーネントと呼ばれ、ルートはコンポーネントをつなぎ合わせて構成していく。

### XMLでのルート実装

以下のコマンドでサンプルのXMLでのルート実装を用意できる。

```
camel init hello.xml
```

中身は以下のようになる。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<routes xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://camel.apache.org/schema/spring"
        xsi:schemaLocation="
            http://camel.apache.org/schema/spring
            https://camel.apache.org/schema/spring/camel-spring.xsd">

    <route>
        <from uri="timer:xml?period=1000"/>
        <setBody>
            <simple>Hello Camel from ${routeId}</simple>
        </setBody>
        <log message="${body}"/>
    </route>

</routes>
```

`routes`タグの中に`route`タグを用意し、各コンポーネントタグを配置する。

### YAMLでのルート定義

以下のコマンドでサンプルのYAMLでのルート実装を用意できる。

```
camel init hello.yaml
```

中身は以下のようになる。

```yaml
- from:
    uri: "timer:yaml"
    parameters:
      period: "1000"
    steps:
      - setBody:
          simple: "Hello Camel from ${routeId}"
      - log: "${body}"
```

エンドポイントは`from`の中に`uri`と`parameters`で具体的になエンドポイント名とエンドポイントのパラメータを指定する。
さらに、`steps`でコンポーネントを記載していく。

### Groovyでのルート定義

以下のコマンドでアンプルのGroovyでのルート実装を用意できる。

```
camel init hello.groovy
```

中身は以下のようになる。

```groovy
from('timer:groovy?period=1000')
  .setBody()
    .simple('Hello Camel from ${routeId}')
  .log('${body}')
```

ちょうど、Javaでのルート実装の`configure()`の中身のみを記載する形になる。

### ルートの実行方法

ルートの実行には以下のように`run`コマンドを用いる。

```
camel run ルート実装ファイル
```

### 複数のファイルでのルート実装および実行

以下のコマンドで`double-hello`ディレクトリを作成し、`hello.groovy`および`hello.yaml`を配置する。

```
mkdir double-hello
touch double-hello/hello.groovy
touch double-hello/hello.yaml
```

そしてそれぞれのファイルで以下のようにルートを定義する。

`hello.groovy`
```groovy
from('timer:groovy?period=1000')
  .setBody()
    .simple('Hello Camel from ${routeId}')
  .log('${body}')
```

`hello.yaml`
```yaml
- from:
    uri: "timer:yaml"
    parameters:
      period: "1000"
    steps:
      - setBody:
          simple: "Hello Camel from ${routeId}"
      - log: "${body}"
```

以下のようにディレクトリを指定して２つのルートを実行することができる。

```
camel run --source-dir=double-hello
```

また、直接２つのファイルを指定して２つのルートを実行することもできる。

```
camel run double-hello/hello.groovy double-hello/hello.yaml
```

### プレースホルダーを用いるルート実装

以下のような`hello.java`を用意する。

```java
import org.apache.camel.builder.RouteBuilder;

public class hello extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:java?period={{time:1000}}")
            .setBody()
                .simple("Hello Camel from {{you}}")
            .log("${body}");
    }
}
```

[Javaでのルート実装](#javaでのルート実装)との違いは以下の部分。

```diff
7c7
<         from("timer:java?period={{time:1000}}")
---
>         from("timer:java?period=1000")
9c9
<                 .simple("Hello Camel from {{you}}")
---
>                 .simple("Hello Camel from ${routeId}")

```

このまま`run`コマンドで実行してもエラーとなる。

`camel run hello.java`を実行して出るエラー
```
org.apache.camel.FailedToCreateRouteException: Failed to create route route1 at: >>> SetBody[simple{Hello Camel from {{you}}}] <<< in route: Route(route1)[From[timer:java?period={{time:1000}}] -> [SetB... because of Property with key [you] not found in properties from text: Hello Camel from {{you}}
```

これはプレースホルダーに何も指定がなされないまま、実行されたためである。

以下のコマンドでプレースホルダーに値をインタラクティブに指定して実行することができる。

```
camel run hello.java --prompt
```

また、以下のように`application.properties`を書いて実行することもできる。

```
you=jack
time=100
```

## ファイルの伝送を行うルートの実装

### プロセス

あるディレクトリ`data/inbox`からファイルを読み込んで別のディレクトリ`data/outbox`に書き込む。

プロセスのイメージ

```
data/inbox -> File -> data/outbox
```

### 実装

以下のファイルを`file-copy/route.java`として作成する。

```java
import org.apache.camel.builder.RouteBuilder;

public class route extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file:data/inbox?noop=true")
            .to("file:data/outbox");
    }
}

```

### 実行

以下のコマンドでcamelルートを実行する。

```
camel run --source-dir file-copy
```

この状態で、`data/indbox`ディレクトリが作成され、`data/inbox`ディレクトリに配置されるファイルの読み込み待ちとなる。

ここで、以下のようなファイル`message1.txt`を`data/inbox`内に作成する。

`data/inbox/message1.txt`
```
hello world!
```

そうすると、`file-copy/route.java`で作成したルートが`message1.txt`を読み取り`data/outbox/message1.txt`としてファイルの内容を保存する。

Note: このとき、`message1.txt`の内容を変更してもルートは変更内容を読み取ることはしないことに注意。

同様に以下のような`data/inbox/message2.txt`を作成すると`data/outbox/message2.txt`がルート側で作成される。

`data/inbox/message2.txt`
```
hello world!!!!!!
```

## MQを処理するルートの実装

### Apache ActiveMQ Artemisを用いた

### 準備

以下のコマンドでApache ActiveMQ Artemisのコンテナイメージからコンテナを作成する。

```
podman run --detach --name mycontainer -p 61616:61616 -p 8161:8161 --rm apache/activemq-artemis:latest-alpine
```

### 実装

以下のように`mq-route`ディレクトリに`consumer.yaml`および`producer.yaml`の2つのファイルを作成する。

`mq-route/consumer.yaml`
```yaml
- from:
    uri: "kamelet:jms-pooled-apache-artemis-source"
    parameters:
      brokerURL: "tcp://localhost:61616"
      destinationName: "queue-test"
      destinationType: "queue"
      username: "artemis"
      password: "artemis"
    steps:
      - to:
          uri: "kamelet:log-sink"
          parameters:
            showHeaders: true
```

`mq-route/producer.yaml`
```yaml
- from:
    uri: "kamelet:timer-source"
    parameters:
      message: '{"id": "1", "message": "Hello Camel  by producer.yaml"}'
      contentType: "applicaiton/json"
      repeatCount: 10
    steps:
      - to:
          uri: "kamelet:jms-pooled-apache-artemis-sink"
          parameters:
            brokerURL: "tcp://localhost:61616"
            destinationName: "queue-test"
            destinationType: "queue"
            username: "artemis"
            password: "artemis"
            maxSessionsPerConnection: 1000
```

### 実行

以下のコマンドで実行する。

```
camel run --source-dir=mq-route
```

## ライブリロードと開発コンソール

`run`コマンド実行時に`--dev`オプションをつける（開発モードを有効にする）ことで、ライブリロードを有効にすることができる

```
camel run hello.yaml --dev
```

