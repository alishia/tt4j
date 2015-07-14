**[TT4J has moved to GitHub. Please visit the new site.](http://reckart.github.io/tt4j/)**

TreeTagger for Java is a Java wrapper around the popular [TreeTagger](http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/) package by Helmut Schmid. It was written with a focus on platform-independence and easy integration into applications. It is written in Java 5 and has been tested on OS X, Ubuntu Linux, and Windows.

<table><tr>
<td></td>
<td><wiki:gadget url="http://www.ohloh.net/p/484337/widgets/project_users.xml" height="100" border="0"/></td>
<td><wiki:gadget url="http://www.ohloh.net/p/484337/widgets/project_thin_badge.xml" height="36" border="0"/></td>
</tr></table>

### Code example ###

```
package org.annolab.tt4j;

import static java.util.Arrays.asList;

public class Example {
  public static void main(String[] args) throws Exception {
    // Point TT4J to the TreeTagger installation directory. The executable is expected
    // in the "bin" subdirectory - in this example at "/opt/treetagger/bin/tree-tagger"
    System.setProperty("treetagger.home", "/opt/treetagger");
    TreeTaggerWrapper tt = new TreeTaggerWrapper<String>();
    try {
      tt.setModel("/opt/treetagger/models/english.par:iso8859-1");
      tt.setHandler(new TokenHandler<String>() {
        public void token(String token, String pos, String lemma) {
          System.out.println(token + "\t" + pos + "\t" + lemma);
        }
      });
      tt.process(asList(new String[] { "This", "is", "a", "test", "." }));
    }
    finally {
      tt.destroy();
    }
  }
}
```

More documentation can be found [in the wiki](Usage.md).<br />
Changes between versions are recorded in the [change log](Changes.md).

The latest version of TT4J is now available via [Maven Central](http://repo1.maven.org/maven2/org/annolab/tt4j/). If you use Maven as your build tool, then you can add it as a dependency in your pom.xml file:

```
<dependency>
  <groupId>org.annolab.tt4j</groupId>
  <artifactId>org.annolab.tt4j</artifactId>
  <version>1.2.1</version>
</dependency>
```

### License ###

**The [TreeTagger](http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/) package, which is wrapped by TT4J, may only be used according to the [TreeTagger license terms](http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/Tagger-Licence)**. TreeTagger for Java is provided under the Apache License version 2.0 without any warranty.