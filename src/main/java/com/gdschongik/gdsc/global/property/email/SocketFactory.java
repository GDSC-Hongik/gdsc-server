package com.gdschongik.gdsc.global.property.email;

import com.gdschongik.gdsc.global.util.Pair;

public record SocketFactory(Pair<Integer> port, Pair<Boolean> fallback, Pair<String> classInfo) {}
