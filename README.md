# scorelist

[![CI](https://github.com/summeriver13/scorelist/actions/workflows/ci.yml/badge.svg)](https://github.com/summeriver13/scorelist/actions/workflows/ci.yml)

Java 17 实现的命令行学生成绩管理程序（课程实验三），按可测试的分层结构组织：记录不可变、规则在 `ScoreTable`、I/O 只在 `Cli`。

## 功能

1. 查看全部成绩记录
2. 添加记录（学生姓名、学号、课程、成绩）
3. 按学号 + 课程修改成绩
4. 按学号 + 课程删除记录
5. 统计某课程的平均分、最高分、最低分
6. 按学号查看某学生的全部成绩
7. 按成绩从高到低输出

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
- `ScoreTable` 不接触 `System.in`/`System.out`，因此可以直接单测
- 代码格式由 Spotless（google-java-format）在 `validate` 阶段检查，`./mvnw spotless:apply` 自动修复

## 参考

课程实验三。早期版本参考 [ZenGx1n/student-score-manage](https://github.com/ZenGx1n/student-score-manage)。
