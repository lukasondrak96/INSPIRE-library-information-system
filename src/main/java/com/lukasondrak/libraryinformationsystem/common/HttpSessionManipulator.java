package com.lukasondrak.libraryinformationsystem.common;

import javax.servlet.http.HttpSession;

public class HttpSessionManipulator {

    public static void clearSessionResultAttribute(HttpSession session) {
        session.setAttribute("result", null);
    }
}
