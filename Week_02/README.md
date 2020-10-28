- #### Serial GC(串行GC)
    - 单线程工作的收集器，执行时STW
    - 新生代: 使用串行回收，复制算法
    - 老年代：使用串行回收，标记-压缩算法
    - 对于运行在客户端模式下的虚拟机来说是一个比较好的选择
```
# 指令
java -Xmx2048m -Xms2048m -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis

# 运行结果
正在执行...
[GC (Allocation Failure) [DefNew: 559232K->69888K(629120K), 0.0507916 secs] 559232K->155445K(2027264K), 0.0512147 secs] [Times: user=0.02 sys=0.03, real=0.05 secs]
[GC (Allocation Failure) [DefNew: 629120K->69887K(629120K), 0.0665861 secs] 714677K->285041K(2027264K), 0.0671923 secs] [Times: user=0.02 sys=0.05, real=0.07 secs]
[GC (Allocation Failure) [DefNew: 629119K->69887K(629120K), 0.0525466 secs] 844273K->405487K(2027264K), 0.0530015 secs] [Times: user=0.02 sys=0.03, real=0.05 secs]
[GC (Allocation Failure) [DefNew: 629119K->69887K(629120K), 0.0503308 secs] 964719K->523807K(2027264K), 0.0508029 secs] [Times: user=0.02 sys=0.03, real=0.05 secs]
[GC (Allocation Failure) [DefNew: 629119K->69887K(629120K), 0.0521967 secs] 1083039K->645927K(2027264K), 0.0529241 secs] [Times: user=0.03 sys=0.03, real=0.05 secs]
[GC (Allocation Failure) [DefNew: 629119K->69887K(629120K), 0.0559744 secs] 1205159K->768954K(2027264K), 0.0568086 secs] [Times: user=0.01 sys=0.05, real=0.06 secs]
[GC (Allocation Failure) [DefNew: 629119K->69887K(629120K), 0.0515524 secs] 1328186K->891323K(2027264K), 0.0519226 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]
[GC (Allocation Failure) [DefNew: 629119K->69887K(629120K), 0.0566967 secs] 1450555K->1028654K(2027264K), 0.0575196 secs] [Times: user=0.03 sys=0.03, real=0.06 secs]
执行结束!共生成对象次数:17069
Heap
 def new generation   total 629120K, used 92378K [0x0000000080000000, 0x00000000aaaa0000, 0x00000000aaaa0000)
  eden space 559232K,   4% used [0x0000000080000000, 0x00000000815f6860, 0x00000000a2220000)
  from space 69888K,  99% used [0x00000000a2220000, 0x00000000a665fff0, 0x00000000a6660000)
  to   space 69888K,   0% used [0x00000000a6660000, 0x00000000a6660000, 0x00000000aaaa0000)
 tenured generation   total 1398144K, used 958767K [0x00000000aaaa0000, 0x0000000100000000, 0x0000000100000000)
   the space 1398144K,  68% used [0x00000000aaaa0000, 0x00000000e52ebc08, 0x00000000e52ebe00, 0x0000000100000000)
 Metaspace       used 2714K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 291K, capacity 386K, committed 512K, reserved 1048576K
  
# 指令
java -Xmx512m -Xms512m -XX:+UseSerialGC -XX:+PrintGCDetails GCLogAnalysis

# 运行结果
正在执行...
[GC (Allocation Failure) [DefNew: 139386K->17472K(157248K), 0.0158930 secs] 139386K->44426K(506816K), 0.0165168 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157101K->17468K(157248K), 0.0239276 secs] 184055K->96918K(506816K), 0.0243773 secs] [Times: user=0.00 sys=0.02, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157244K->17471K(157248K), 0.0176713 secs] 236694K->138006K(506816K), 0.0182344 secs] [Times: user=0.00 sys=0.01, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157026K->17469K(157248K), 0.0189856 secs] 277560K->178985K(506816K), 0.0196571 secs] [Times: user=0.00 sys=0.02, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157141K->17471K(157248K), 0.0173994 secs] 318657K->217525K(506816K), 0.0179516 secs] [Times: user=0.01 sys=0.00, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157247K->17470K(157248K), 0.0191123 secs] 357301K->259312K(506816K), 0.0199960 secs] [Times: user=0.01 sys=0.02, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157246K->17471K(157248K), 0.0218032 secs] 399088K->307436K(506816K), 0.0224898 secs] [Times: user=0.00 sys=0.02, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157247K->17471K(157248K), 0.0221247 secs] 447212K->358433K(506816K), 0.0223671 secs] [Times: user=0.02 sys=0.02, real=0.02 secs]
[GC (Allocation Failure) [DefNew: 157247K->157247K(157248K), 0.0004194 secs][Tenured: 340961K->281426K(349568K), 0.0413101 secs] 498209K->281426K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0438753 secs] [Times: user=0.03 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139776K->17470K(157248K), 0.0070433 secs] 421202K->325903K(506816K), 0.0074583 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [DefNew: 157173K->157173K(157248K), 0.0002946 secs][Tenured: 308432K->307298K(349568K), 0.0428569 secs] 465606K->307298K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0448266 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0007919 secs][Tenured: 307298K->315182K(349568K), 0.0421928 secs] 447074K->315182K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0449388 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0004387 secs][Tenured: 315182K->317119K(349568K), 0.0468717 secs] 454958K->317119K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0481050 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0018995 secs][Tenured: 317119K->342989K(349568K), 0.0340527 secs] 456895K->342989K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0373977 secs] [Times: user=0.03 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0004424 secs][Tenured: 342989K->349332K(349568K), 0.0422077 secs] 482765K->349332K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0433475 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [DefNew: 139599K->139599K(157248K), 0.0003670 secs][Tenured: 349332K->349117K(349568K), 0.0459714 secs] 488931K->350067K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0474733 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]
[Full GC (Allocation Failure) [Tenured: 349415K->337704K(349568K), 0.0511658 secs] 506654K->337704K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0529543 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]
[GC (Allocation Failure) [DefNew: 139776K->139776K(157248K), 0.0013837 secs][Tenured: 337704K->349486K(349568K), 0.0304973 secs] 477480K->355086K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0340654 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
[Full GC (Allocation Failure) [Tenured: 349554K->349431K(349568K), 0.0407037 secs] 506798K->359998K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0417299 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
[Full GC (Allocation Failure) [Tenured: 349431K->349372K(349568K), 0.0461118 secs] 506571K->364872K(506816K), [Metaspace: 2708K->2708K(1056768K)], 0.0465604 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]
执行结束!共生成对象次数:10735
Heap
 def new generation   total 157248K, used 21368K [0x00000000e0000000, 0x00000000eaaa0000, 0x00000000eaaa0000)
  eden space 139776K,  15% used [0x00000000e0000000, 0x00000000e14de0b8, 0x00000000e8880000)
  from space 17472K,   0% used [0x00000000e9990000, 0x00000000e9990000, 0x00000000eaaa0000)
  to   space 17472K,   0% used [0x00000000e8880000, 0x00000000e8880000, 0x00000000e9990000)
 tenured generation   total 349568K, used 349372K [0x00000000eaaa0000, 0x0000000100000000, 0x0000000100000000)
   the space 349568K,  99% used [0x00000000eaaa0000, 0x00000000fffcf3a0, 0x00000000fffcf400, 0x0000000100000000)
 Metaspace       used 2714K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 291K, capacity 386K, committed 512K, reserved 1048576K
```

- #### Parallel GC(并行GC)
    - 多个线程执行垃圾回收，执行时STW
    - 适合吞吐量系统
    - 新生代：复制算法
    - 老年代：标记整理算法
    - 默认开启GC线程数与CPU核心数相同，在CPU核心数较多物理机上时可通过-XX:ParallelGCThreads=N设置GC线程数
```
# 指令
java -Xmx2048m -Xms2048m -XX:+UseParallelGC -XX:+PrintGCDetails GCLogAnalysis

# 运行结果
正在执行...
[GC (Allocation Failure) [PSYoungGen: 524800K->87026K(611840K)] 524800K->152853K(2010112K), 0.0246087 secs] [Times: user=0.06 sys=0.17, real=0.02 secs]
[GC (Allocation Failure) [PSYoungGen: 611826K->87027K(611840K)] 677653K->275796K(2010112K), 0.0329419 secs] [Times: user=0.11 sys=0.11, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 611827K->87027K(611840K)] 800596K->389389K(2010112K), 0.0292409 secs] [Times: user=0.05 sys=0.08, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 611827K->87027K(611840K)] 914189K->494420K(2010112K), 0.0259726 secs] [Times: user=0.13 sys=0.03, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 611785K->87037K(611840K)] 1019177K->621537K(2010112K), 0.0310382 secs] [Times: user=0.02 sys=0.20, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 611837K->87039K(320000K)] 1146337K->742783K(1718272K), 0.0301495 secs] [Times: user=0.09 sys=0.14, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 319999K->142093K(465920K)] 975743K->805045K(1864192K), 0.0154723 secs] [Times: user=0.09 sys=0.01, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 375053K->170722K(465920K)] 1038005K->847534K(1864192K), 0.0189211 secs] [Times: user=0.13 sys=0.00, real=0.02 secs]
[GC (Allocation Failure) [PSYoungGen: 403682K->181318K(465920K)] 1080494K->887601K(1864192K), 0.0221129 secs] [Times: user=0.20 sys=0.05, real=0.02 secs]
[GC (Allocation Failure) [PSYoungGen: 414278K->129754K(465920K)] 1120561K->915305K(1864192K), 0.0243882 secs] [Times: user=0.08 sys=0.03, real=0.02 secs]
[GC (Allocation Failure) [PSYoungGen: 362714K->75775K(465920K)] 1148265K->964187K(1864192K), 0.0238589 secs] [Times: user=0.06 sys=0.05, real=0.02 secs]
[GC (Allocation Failure) [PSYoungGen: 308735K->72173K(465920K)] 1197147K->1021808K(1864192K), 0.0165338 secs] [Times: user=0.06 sys=0.05, real=0.02 secs]
[GC (Allocation Failure) [PSYoungGen: 305133K->73841K(465920K)] 1254768K->1080116K(1864192K), 0.0162226 secs] [Times: user=0.03 sys=0.09, real=0.02 secs]
执行结束!共生成对象次数:18942
Heap
 PSYoungGen      total 465920K, used 297363K [0x00000000d5580000, 0x0000000100000000, 0x0000000100000000)
  eden space 232960K, 95% used [0x00000000d5580000,0x00000000e2fc8958,0x00000000e3900000)
  from space 232960K, 31% used [0x00000000e3900000,0x00000000e811c600,0x00000000f1c80000)
  to   space 232960K, 0% used [0x00000000f1c80000,0x00000000f1c80000,0x0000000100000000)
 ParOldGen       total 1398272K, used 1006275K [0x0000000080000000, 0x00000000d5580000, 0x00000000d5580000)
  object space 1398272K, 71% used [0x0000000080000000,0x00000000bd6b0ce0,0x00000000d5580000)
 Metaspace       used 2717K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 291K, capacity 386K, committed 512K, reserved 1048576K
  
# 指令
java -Xmx512m -Xms512m -XX:+UseParallelGC -XX:+PrintGCDetails GCLogAnalysis

# 运行结果
正在执行...
[GC (Allocation Failure) [PSYoungGen: 131584K->21496K(153088K)] 131584K->43355K(502784K), 0.0064777 secs] [Times: user=0.02 sys=0.02, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 153080K->21486K(153088K)] 174939K->91818K(502784K), 0.0106038 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 153070K->21496K(153088K)] 223402K->136440K(502784K), 0.0090123 secs] [Times: user=0.03 sys=0.08, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 153080K->21490K(153088K)] 268024K->181858K(502784K), 0.0094837 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 153074K->21501K(153088K)] 313442K->217945K(502784K), 0.0076347 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 153085K->21496K(80384K)] 349529K->257176K(430080K), 0.0088311 secs] [Times: user=0.01 sys=0.09, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 80376K->36064K(116736K)] 316056K->275380K(466432K), 0.0045261 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 94944K->42985K(116736K)] 334260K->288806K(466432K), 0.0074347 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 101865K->50535K(116736K)] 347686K->305010K(466432K), 0.0071606 secs] [Times: user=0.00 sys=0.02, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 109255K->38731K(116736K)] 363730K->324214K(466432K), 0.0094308 secs] [Times: user=0.03 sys=0.08, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 97474K->21655K(116736K)] 382957K->343982K(466432K), 0.0077111 secs] [Times: user=0.02 sys=0.06, real=0.01 secs]
[Full GC (Ergonomics) [PSYoungGen: 21655K->0K(116736K)] [ParOldGen: 322327K->239698K(349696K)] 343982K->239698K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0282943 secs] [Times: user=0.08 sys=0.03, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58880K->23852K(116736K)] 298578K->263550K(466432K), 0.0042169 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 82732K->19731K(116736K)] 322430K->282569K(466432K), 0.0061192 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 78527K->21806K(116736K)] 341365K->303234K(466432K), 0.0041910 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 80686K->18281K(116736K)] 362114K->319649K(466432K), 0.0045749 secs] [Times: user=0.09 sys=0.00, real=0.00 secs]
[Full GC (Ergonomics) [PSYoungGen: 18281K->0K(116736K)] [ParOldGen: 301368K->268419K(349696K)] 319649K->268419K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0323986 secs] [Times: user=0.13 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58810K->21448K(116736K)] 327229K->289868K(466432K), 0.0039493 secs] [Times: user=0.08 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 80060K->21201K(116736K)] 348480K->309342K(466432K), 0.0049120 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 80052K->20608K(116736K)] 368193K->328877K(466432K), 0.0051174 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[Full GC (Ergonomics) [PSYoungGen: 20608K->0K(116736K)] [ParOldGen: 308268K->280503K(349696K)] 328877K->280503K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0333667 secs] [Times: user=0.19 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58780K->19664K(116736K)] 339284K->300168K(466432K), 0.0025748 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 78357K->22811K(116736K)] 358861K->320758K(466432K), 0.0047393 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 81691K->19364K(116736K)] 379638K->338988K(466432K), 0.0067181 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[Full GC (Ergonomics) [PSYoungGen: 19364K->0K(116736K)] [ParOldGen: 319624K->297875K(349696K)] 338988K->297875K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0355607 secs] [Times: user=0.13 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [PSYoungGen: 58880K->24944K(116736K)] 356755K->322820K(466432K), 0.0058441 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [PSYoungGen: 83824K->21542K(116736K)] 381700K->343465K(466432K), 0.0069298 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[Full GC (Ergonomics) [PSYoungGen: 21542K->0K(116736K)] [ParOldGen: 321922K->311266K(349696K)] 343465K->311266K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0347010 secs] [Times: user=0.22 sys=0.00, real=0.03 secs]
[GC (Allocation Failure) [PSYoungGen: 58501K->18294K(116736K)] 369768K->329561K(466432K), 0.0040256 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 77174K->16506K(116736K)] 388441K->345548K(466432K), 0.0044405 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[Full GC (Ergonomics) [PSYoungGen: 16506K->0K(116736K)] [ParOldGen: 329042K->315537K(349696K)] 345548K->315537K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0383859 secs] [Times: user=0.17 sys=0.00, real=0.04 secs]
[Full GC (Ergonomics) [PSYoungGen: 58610K->0K(116736K)] [ParOldGen: 315537K->313536K(349696K)] 374147K->313536K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0361957 secs] [Times: user=0.19 sys=0.00, real=0.04 secs]
[GC (Allocation Failure) [PSYoungGen: 58808K->21521K(116736K)] 372345K->335057K(466432K), 0.0039694 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [PSYoungGen: 80401K->16215K(116736K)] 393937K->350851K(466432K), 0.0045395 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[Full GC (Ergonomics) [PSYoungGen: 16215K->0K(116736K)] [ParOldGen: 334636K->324093K(349696K)] 350851K->324093K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0391141 secs] [Times: user=0.22 sys=0.00, real=0.04 secs]
[Full GC (Ergonomics) [PSYoungGen: 58470K->0K(116736K)] [ParOldGen: 324093K->322893K(349696K)] 382563K->322893K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0375306 secs] [Times: user=0.13 sys=0.00, real=0.04 secs]
[Full GC (Ergonomics) [PSYoungGen: 58753K->0K(116736K)] [ParOldGen: 322893K->324595K(349696K)] 381646K->324595K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0363202 secs] [Times: user=0.31 sys=0.00, real=0.04 secs]
[Full GC (Ergonomics) [PSYoungGen: 58753K->0K(116736K)] [ParOldGen: 324595K->326613K(349696K)] 383349K->326613K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0352512 secs] [Times: user=0.11 sys=0.00, real=0.04 secs]
[Full GC (Ergonomics) [PSYoungGen: 58880K->0K(116736K)] [ParOldGen: 326613K->330322K(349696K)] 385493K->330322K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0373428 secs] [Times: user=0.20 sys=0.00, real=0.04 secs]
[Full GC (Ergonomics) [PSYoungGen: 58880K->0K(116736K)] [ParOldGen: 330322K->334508K(349696K)] 389202K->334508K(466432K), [Metaspace: 2710K->2710K(1056768K)], 0.0362091 secs] [Times: user=0.13 sys=0.02, real=0.04 secs]
执行结束!共生成对象次数:9058
Heap
 PSYoungGen      total 116736K, used 21939K [0x00000000f5580000, 0x0000000100000000, 0x0000000100000000)
  eden space 58880K, 37% used [0x00000000f5580000,0x00000000f6aecfe0,0x00000000f8f00000)
  from space 57856K, 0% used [0x00000000f8f00000,0x00000000f8f00000,0x00000000fc780000)
  to   space 57856K, 0% used [0x00000000fc780000,0x00000000fc780000,0x0000000100000000)
 ParOldGen       total 349696K, used 334508K [0x00000000e0000000, 0x00000000f5580000, 0x00000000f5580000)
  object space 349696K, 95% used [0x00000000e0000000,0x00000000f46ab168,0x00000000f5580000)
 Metaspace       used 2717K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 291K, capacity 386K, committed 512K, reserved 1048576K
```

- #### 并发GC(CMS GC)
    - 基于标记清除算法
    - 分四个步骤：
        - 初始标记(CMS initial mark)，执行时STW
        - 并发标记(CMS concurrent mark)
        - 重新标记(CMS remark)，执行时STW
        - 并发清除(CMS concurrent sweep)
    - 优点：并发收集、低停顿
    - 缺点：
        - 对处理器资源非常敏感，默认启动回收线程数为（CPU核心数量 + 3） / 4 
        - 无法处理“浮动垃圾”有可能并发失败而导致Full GC
        - 基于标记清除算法会产生大量空间碎片
```
# 指令
java -Xmx2048m -Xms2048m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails GCLogAnalysis

# 运行结果
正在执行...
[GC (Allocation Failure) [ParNew: 545344K->68096K(613440K), 0.0260884 secs] 545344K->152085K(2029056K), 0.0274915 secs] [Times: user=0.05 sys=0.03, real=0.03 secs]
[GC (Allocation Failure) [ParNew: 613440K->68096K(613440K), 0.0307115 secs] 697429K->276700K(2029056K), 0.0313911 secs] [Times: user=0.06 sys=0.08, real=0.03 secs]
[GC (Allocation Failure) [ParNew: 613440K->68096K(613440K), 0.0517661 secs] 822044K->402952K(2029056K), 0.0522322 secs] [Times: user=0.23 sys=0.05, real=0.05 secs]
[GC (Allocation Failure) [ParNew: 613440K->68096K(613440K), 0.0512941 secs] 948296K->525876K(2029056K), 0.0516706 secs] [Times: user=0.41 sys=0.03, real=0.05 secs]
[GC (Allocation Failure) [ParNew: 613440K->68096K(613440K), 0.0498962 secs] 1071220K->643756K(2029056K), 0.0502438 secs] [Times: user=0.33 sys=0.05, real=0.05 secs]
[GC (Allocation Failure) [ParNew: 613440K->68094K(613440K), 0.0522550 secs] 1189100K->772471K(2029056K), 0.0526009 secs] [Times: user=0.33 sys=0.02, real=0.05 secs]
[GC (Allocation Failure) [ParNew: 613438K->68096K(613440K), 0.0534669 secs] 1317815K->900123K(2029056K), 0.0537358 secs] [Times: user=0.31 sys=0.09, real=0.05 secs]
[GC (CMS Initial Mark) [1 CMS-initial-mark: 832027K(1415616K)] 900260K(2029056K), 0.0003484 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.005/0.005 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-abortable-preclean-start]
[GC (Allocation Failure) [ParNew[CMS-concurrent-abortable-preclean: 0.002/0.102 secs]: [Times: user=0.27 sys=0.05, real=0.11 secs]
 613440K->68096K(613440K), 0.0538362 secs] 1445467K->1023661K(2029056K), 0.0540965 secs] [Times: user=0.20 sys=0.05, real=0.05 secs]
[GC (CMS Final Remark) [YG occupancy: 78975 K (613440 K)][Rescan (parallel) , 0.0017018 secs][weak refs processing, 0.0001360 secs][class unloading, 0.0002870 secs][scrub symbol table, 0.0003088 secs][scrub string table, 0.0001281 secs][1 CMS-remark: 955565K(1415616K)] 1034541K(2029056K), 0.0033273 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
[CMS-concurrent-sweep-start]
[CMS-concurrent-sweep: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.004/0.004 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
执行结束!共生成对象次数:16480
Heap
 par new generation   total 613440K, used 145246K [0x0000000080000000, 0x00000000a9990000, 0x00000000a9990000)
  eden space 545344K,  14% used [0x0000000080000000, 0x0000000084b57b88, 0x00000000a1490000)
  from space 68096K, 100% used [0x00000000a1490000, 0x00000000a5710000, 0x00000000a5710000)
  to   space 68096K,   0% used [0x00000000a5710000, 0x00000000a5710000, 0x00000000a9990000)
 concurrent mark-sweep generation total 1415616K, used 419523K [0x00000000a9990000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 2717K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 291K, capacity 386K, committed 512K, reserved 1048576K
  
# 指令
java -Xmx1024m -Xms1024m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails GCLogAnalysis

# 运行结果
正在执行...
[GC (Allocation Failure) [ParNew: 279616K->34943K(314560K), 0.0156308 secs] 279616K->90690K(1013632K), 0.0177834 secs] [Times: user=0.02 sys=0.02, real=0.02 secs]
[GC (Allocation Failure) [ParNew: 314559K->34944K(314560K), 0.0160817 secs] 370306K->167352K(1013632K), 0.0163298 secs] [Times: user=0.05 sys=0.08, real=0.02 secs]
[GC (Allocation Failure) [ParNew: 314560K->34942K(314560K), 0.0264853 secs] 446968K->241335K(1013632K), 0.0272107 secs] [Times: user=0.14 sys=0.05, real=0.03 secs]
[GC (Allocation Failure) [ParNew: 314558K->34944K(314560K), 0.0211908 secs] 520951K->317195K(1013632K), 0.0214663 secs] [Times: user=0.05 sys=0.03, real=0.02 secs]
[GC (Allocation Failure) [ParNew: 314560K->34942K(314560K), 0.0231527 secs] 596811K->389302K(1013632K), 0.0235195 secs] [Times: user=0.08 sys=0.02, real=0.02 secs]
[GC (CMS Initial Mark) [1 CMS-initial-mark: 354359K(699072K)] 389720K(1013632K), 0.0004477 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-abortable-preclean-start]
[GC (Allocation Failure) [ParNew: 314558K->34942K(314560K), 0.0247512 secs] 668918K->470846K(1013632K), 0.0249984 secs] [Times: user=0.22 sys=0.03, real=0.03 secs]
[GC (Allocation Failure) [ParNew: 314184K->34942K(314560K), 0.0215148 secs] 750088K->546117K(1013632K), 0.0217826 secs] [Times: user=0.03 sys=0.03, real=0.02 secs]
[GC (Allocation Failure) [ParNew: 314558K->34944K(314560K), 0.0261342 secs] 825733K->631376K(1013632K), 0.0263635 secs] [Times: user=0.14 sys=0.05, real=0.03 secs]
[GC (Allocation Failure) [ParNew: 314560K->34943K(314560K), 0.0193119 secs] 910992K->703643K(1013632K), 0.0197636 secs] [Times: user=0.11 sys=0.00, real=0.02 secs]
[CMS-concurrent-abortable-preclean: 0.005/0.217 secs] [Times: user=0.63 sys=0.11, real=0.22 secs]
[GC (CMS Final Remark) [YG occupancy: 40781 K (314560 K)][Rescan (parallel) , 0.0006131 secs][weak refs processing, 0.0000552 secs][class unloading, 0.0002505 secs][scrub symbol table, 0.0003259 secs][scrub string table, 0.0001921 secs][1 CMS-remark: 668700K(699072K)] 709481K(1013632K), 0.0020467 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-sweep-start]
[CMS-concurrent-sweep: 0.001/0.001 secs] [Times: user=0.03 sys=0.00, real=0.00 secs]
[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [ParNew: 314559K->34943K(314560K), 0.0168765 secs] 864740K->673845K(1013632K), 0.0173216 secs] [Times: user=0.20 sys=0.02, real=0.02 secs]
[GC (CMS Initial Mark) [1 CMS-initial-mark: 638901K(699072K)] 674212K(1013632K), 0.0003883 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-abortable-preclean-start]
[CMS-concurrent-abortable-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (CMS Final Remark) [YG occupancy: 63337 K (314560 K)][Rescan (parallel) , 0.0002795 secs][weak refs processing, 0.0000522 secs][class unloading, 0.0002239 secs][scrub symbol table, 0.0002879 secs][scrub string table, 0.0001405 secs][1 CMS-remark: 638901K(699072K)] 702238K(1013632K), 0.0017783 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-sweep-start]
[CMS-concurrent-sweep: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[GC (Allocation Failure) [ParNew: 314559K->34943K(314560K), 0.0132994 secs] 629439K->423305K(1013632K), 0.0135528 secs] [Times: user=0.08 sys=0.03, real=0.01 secs]
[GC (CMS Initial Mark) [1 CMS-initial-mark: 388362K(699072K)] 423341K(1013632K), 0.0002833 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-abortable-preclean-start]
[GC (Allocation Failure) [ParNew: 314559K->34943K(314560K), 0.0148818 secs] 702921K->506151K(1013632K), 0.0151233 secs] [Times: user=0.08 sys=0.02, real=0.02 secs]
[GC (Allocation Failure) [ParNew: 314559K->34942K(314560K), 0.0132915 secs] 785767K->584392K(1013632K), 0.0135487 secs] [Times: user=0.11 sys=0.00, real=0.01 secs]
[GC (Allocation Failure) [ParNew: 314558K->34943K(314560K), 0.0159719 secs] 864008K->661954K(1013632K), 0.0162576 secs] [Times: user=0.01 sys=0.00, real=0.02 secs]
[GC (Allocation Failure) [ParNew: 314559K->314559K(314560K), 0.0009239 secs][CMS[CMS-concurrent-abortable-preclean: 0.007/0.168 secs] [Times: user=0.33 sys=0.02, real=0.17 secs]
 (concurrent mode failure): 627011K->361340K(699072K), 0.0564526 secs] 941570K->361340K(1013632K), [Metaspace: 2710K->2710K(1056768K)], 0.0579693 secs] [Times: user=0.05 sys=0.00, real=0.06 secs]
[GC (Allocation Failure) [ParNew: 279616K->34942K(314560K), 0.0124182 secs] 640956K->447400K(1013632K), 0.0126373 secs] [Times: user=0.09 sys=0.01, real=0.01 secs]
[GC (Allocation Failure) [ParNew: 314557K->34943K(314560K), 0.0156223 secs] 727014K->524892K(1013632K), 0.0159068 secs] [Times: user=0.08 sys=0.00, real=0.02 secs]
[GC (CMS Initial Mark) [1 CMS-initial-mark: 489948K(699072K)] 525020K(1013632K), 0.0002851 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.03 sys=0.00, real=0.00 secs]
[CMS-concurrent-abortable-preclean-start]
执行结束!共生成对象次数:18526
Heap
 par new generation   total 314560K, used 160406K [0x00000000c0000000, 0x00000000d5550000, 0x00000000d5550000)
  eden space 279616K,  44% used [0x00000000c0000000, 0x00000000c7a85b28, 0x00000000d1110000)
  from space 34944K,  99% used [0x00000000d1110000, 0x00000000d332ff30, 0x00000000d3330000)
  to   space 34944K,   0% used [0x00000000d3330000, 0x00000000d3330000, 0x00000000d5550000)
 concurrent mark-sweep generation total 699072K, used 489948K [0x00000000d5550000, 0x0000000100000000, 0x0000000100000000)
 Metaspace       used 2717K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 291K, capacity 386K, committed 512K, reserved 1048576K
```

- #### G1 GC
    - 垃圾回收技术发展史上的里程碑，开创了面向局部收集的设计思路和基于Region的内存布局形式
    - 面向堆内存任何部分: 新生代、老年代、整堆, 来组成回收集
    - 超过Region容量一般的对象判定为大对象，进入特殊的Humongous区域
    - Regon取值范围为1MB~32MB且为2的N次方
    - 优点：可控的延迟时间，低延迟，基本无碎片，堆内存在6~8G以上较为合适
    - 缺点：跨代之间引用产生的卡表记忆集占用较多额外内存，处理也较复杂
```
# 指令
java -Xmx2048m -Xms2048m -XX:+UseG1GC -XX:+PrintGCDetails GCLogAnalysis

# 运行结果
正在执行...
[GC pause (G1 Evacuation Pause) (young), 0.0052666 secs]
   [Parallel Time: 3.5 ms, GC Workers: 8]
      [GC Worker Start (ms): Min: 30149.0, Avg: 30149.1, Max: 30149.1, Diff: 0.1]
      [Ext Root Scanning (ms): Min: 0.1, Avg: 0.1, Max: 0.2, Diff: 0.1, Sum: 0.8]
      [Update RS (ms): Min: 0.2, Avg: 0.2, Max: 0.3, Diff: 0.1, Sum: 1.7]
         [Processed Buffers: Min: 3, Avg: 3.6, Max: 4, Diff: 1, Sum: 29]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 2.9, Avg: 3.0, Max: 3.1, Diff: 0.2, Sum: 24.3]
      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.7]
         [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.2]
      [GC Worker Total (ms): Min: 3.4, Avg: 3.5, Max: 3.5, Diff: 0.1, Sum: 27.8]
      [GC Worker End (ms): Min: 30152.6, Avg: 30152.6, Max: 30152.6, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.6 ms]
   [Other: 1.1 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.1 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.5 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 102.0M(102.0M)->0.0B(89.0M) Survivors: 0.0B->13.0M Heap: 126.5M(2048.0M)->40.0M(2048.0M)]
 [Times: user=0.00 sys=0.00, real=0.01 secs]
 
 …………
 
[GC pause (G1 Evacuation Pause) (mixed), 0.0054965 secs]
   [Parallel Time: 4.2 ms, GC Workers: 8]
      [GC Worker Start (ms): Min: 31038.9, Avg: 31038.9, Max: 31039.0, Diff: 0.0]
      [Ext Root Scanning (ms): Min: 0.1, Avg: 0.1, Max: 0.1, Diff: 0.0, Sum: 0.7]
      [Update RS (ms): Min: 0.3, Avg: 0.4, Max: 0.4, Diff: 0.0, Sum: 2.9]
         [Processed Buffers: Min: 2, Avg: 5.0, Max: 6, Diff: 4, Sum: 40]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 3.5, Avg: 3.5, Max: 3.6, Diff: 0.1, Sum: 28.3]
      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.9]
         [Termination Attempts: Min: 1, Avg: 1.1, Max: 2, Diff: 1, Sum: 9]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 4.1, Avg: 4.1, Max: 4.2, Diff: 0.1, Sum: 33.0]
      [GC Worker End (ms): Min: 31043.0, Avg: 31043.1, Max: 31043.1, Diff: 0.1]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.2 ms]
   [Other: 1.1 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.1 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.3 ms]
      [Humongous Register: 0.1 ms]
      [Humongous Reclaim: 0.1 ms]
      [Free CSet: 0.1 ms]
   [Eden: 91.0M(91.0M)->0.0B(1022.0M) Survivors: 11.0M->13.0M Heap: 697.8M(2048.0M)->595.1M(2048.0M)]
 [Times: user=0.00 sys=0.00, real=0.01 secs]
执行结束!共生成对象次数:17046
Heap
 garbage-first heap   total 2097152K, used 1054098K [0x0000000080000000, 0x0000000080104000, 0x0000000100000000)
  region size 1024K, 366 young (374784K), 13 survivors (13312K)
 Metaspace       used 2717K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 291K, capacity 386K, committed 512K, reserved 1048576K
```
- #### 使用Apache AB 工具对gateway-server-0.0.1-SNAPSHOT.jar进行压测
```
# 指令
java -jar -Xmx512m -Xms512m gateway-server-0.0.1-SNAPSHOT.jar
ab -c 10 -t 60 http://127.0.0.1:8088/api/hello

# 结果
……
Concurrency Level:      10
Time taken for tests:   31.119 seconds
Complete requests:      50000
Failed requests:        0
Requests per second:    1606.76 [#/sec] (mean)
……

# 指令
java -jar -Xmx1024m -Xms1024m gateway-server-0.0.1-SNAPSHOT.jar
ab -c 10 -t 60 http://127.0.0.1:8088/api/hello

# 结果
……
Concurrency Level:      10
Time taken for tests:   30.030 seconds
Complete requests:      50000
Failed requests:        0
Requests per second:    1665.00 [#/sec] (mean)
……
```

- #### 使用浏览器对基于单线程的HttpServer:8801端口进行测试
```
# 浏览器访问: http://127.0.0.1:8801/
    浏览器返回显示: hello, nio
    
# 控制台输出：
request client: /127.0.0.1:57839
request data:
GET / HTTP/1.1
Host: 127.0.0.1:8801
Connection: keep-alive
Cache-Control: max-age=0
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36 Maxthon/5.2.7.5000
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
DNT: 1
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN
# 通过以上结果可以看出浏览器在请求Http协议时发送的请求数据

```