package com.gdschongik.gdsc.global.util;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;

@RequiredArgsConstructor
public class DiscordUtil {

    private final JDA jda;

    public Role findRoleByName(String roleName) {
        return jda.getRolesByName(roleName, true).stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.DISCORD_ROLE_NOT_FOUND));
    }
}
