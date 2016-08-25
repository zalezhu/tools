/*
 * Copyright (c) 2013. Kevin Lee (http://182.92.183.142/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zale.shortlink.exception;

@SuppressWarnings("serial")
public class EncException extends RuntimeException {
	
    private int id; // a unique id
    private String classname; // the name of the class
    private String method; // the name of the method
    private String message; // a detailed message
    private EncException previous = null; // the exception which was caught
    private String separator = "\n"; // line separator

    public String getMessage() {
        return message;
    }

    public EncException(int id, String classname, String method,
                        String message, EncException previous) {
        this.id = id;
        this.classname = classname;
        this.method = method;
        this.message = message;
        this.previous = previous;
    }

    public String traceBack() {
        return traceBack("\n");
    }

    public String traceBack(String sep) {
        this.separator = sep;
        int level = 0;
        EncException e = this;
        String text = line("Calling sequence (top to bottom)");
        while (e != null) {
            level++;
            text += line("--level " + level + "--------------------------------------");
            text += line("Class/Method: " + e.classname + "/" + e.method);
            text += line("Id          : " + e.id);
            text += line("Message     : " + e.message);
            e = e.previous;
        }
        return text;
    }

    private String line(String s) {
        return s + separator;
    }

}
