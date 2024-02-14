package com.gdschongik.gdsc.global.property.email;

import com.gdschongik.gdsc.global.util.Pair;

public record JavaMailProperty(Pair<Boolean> smtpAuth, SocketFactory socketFactory) {}
