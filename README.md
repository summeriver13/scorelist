# scorelist

[![CI](https://github.com/summeriver13/scorelist/actions/workflows/ci.yml/badge.svg)](https://github.com/summeriver13/scorelist/actions/workflows/ci.yml)
[![Java 17](https://img.shields.io/badge/Java-17-orange)](https://github.com/summeriver13/scorelist/blob/main/pom.xml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Java 17 写的命令行学生成绩管理程序（课程实验三）。分层做得很薄：记录不可变、规则在 `ScoreTable`、I/O 只在 `Cli`，所以核心逻辑不碰 `System.in` 也能测。

## 功能

| 能力 | 说明 |
| --- | --- |
| 增删改查 | 以「学号 + 课程」为一行的唯一键，重复添加会被拒绝 |
| 课程统计 | 某门课的人数、平均分、最高分、最低分 |
| 学生查询 | 按学号列出该生所有课程的成绩 |
| 排序输出 | 按成绩从高到低打印全部记录 |

覆盖课程实验三的 8 项要求，见 `CliTest` 与 `ScoreTableTest`。

## 示例

菜单行已省略：

```text
1 show all  2 add  3 update  4 delete
5 course stats  6 student scores  7 sort by score  0 exit
Enter your choice: 2
Course: math
Student name: Ann
Student id: 1001
Score (0-100): 90
Added.

Enter your choice: 5
Course: math
count=2 average=92.50 max=95.0 min=90.0

Enter your choice: 7
COURSE    NAME      ID           SCORE
math      Bob       1002          95.0
math      Ann       1001          90.0

Enter your choice: 0
Bye.
```

## 运行

需要 JDK 17+，Maven 由 wrapper 自动下载：

```bash
./mvnw -q compile exec:java      # Windows 用 mvnw.cmd
```

## 测试

```bash
./mvnw -q test
```

## 结构

```text
pom.xml
mvnw / mvnw.cmd / .mvn/wrapper/    Maven wrapper，无需预装 Maven
.github/workflows/ci.yml          push 与 PR 跑 verify
src/main/java/scorelist/
├── App.java         入口，装配 Cli
├── Cli.java         菜单、输入校验、输出
├── Score.java       不可变记录，构造时校验非空与 0-100
├── ScoreTable.java  增删改查、统计、排序
└── CourseStats.java 课程统计结果
src/test/java/scorelist/
├── ScoreTest.java
├── ScoreTableTest.java
└── CliTest.java
```

## 约定

- 成绩范围 0–100，非法输入在构造 `Score` 时即抛 `IllegalArgumentException`
- 同一学生同一课程只有一条记录，重复添加抛 `IllegalStateException`
- 代码格式由 Spotless（google-java-format）在 `validate` 阶段检查，`./mvnw spotless:apply` 自动修复

## 参考

课程实验三。早期版本参考 [ZenGx1n/student-score-manage](https://github.com/ZenGx1n/student-score-manage)。

## License

[MIT](LICENSE)
