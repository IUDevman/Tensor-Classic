package dev.tensor.misc.imp.betafix;

/**
 * @author tweska
 * @since 2023
 * THIS CODE IS LICENCED UNDER THE MIT LICENSE AT:
 * https://github.com/tweska/BetaFix/blob/master/LICENSE
 */

public final class BetaFixPlayer {

    public final String username;
    public final String uuid;
    public final String skinURL;
    public final String capeURL;

    public BetaFixPlayer(String username, String uuid, String skinURL, String capeURL) {
        this.username = username;
        this.uuid = uuid;
        this.skinURL = skinURL;
        this.capeURL = capeURL;
    }
}