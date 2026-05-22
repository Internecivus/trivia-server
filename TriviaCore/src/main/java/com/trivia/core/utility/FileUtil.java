package com.trivia.core.utility;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;



public class FileUtil {
    public final static Path SERVER_DIR = Paths.get(System.getProperty("jboss.server.base.dir"));
    public final static Charset CHARSET_DEFAULT = Charset.forName("UTF-8");
}
