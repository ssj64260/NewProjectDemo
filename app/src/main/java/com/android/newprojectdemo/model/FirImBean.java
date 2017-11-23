package com.android.newprojectdemo.model;

import java.io.Serializable;

/**
 * http://api.fir.im/apps/latest/593b5d9eca87a878050001ac?api_token=82ccd3b7d0c62f78581ca306e1f06fd9
 * http://api.fir.im/apps/latest/       应用 ID          ?api_token=            API Token
 * {
 * "name": "新 A+物流",
 * "version": "1",
 * "changelog": "",
 * "updated_at": 1503066563,
 * "versionShort": "1.0",
 * "build": "1",
 * "installUrl": "http:\/\/download.fir.im\/v2\/app\/install\/5996f9ab548b7a129a000052?download_token=f2d4a7c0cab858de45dddef4455b0f11&source=update",
 * "install_url": "http:\/\/download.fir.im\/v2\/app\/install\/5996f9ab548b7a129a000052?download_token=f2d4a7c0cab858de45dddef4455b0f11&source=update",
 * "direct_install_url": "http:\/\/download.fir.im\/v2\/app\/install\/5996f9ab548b7a129a000052?download_token=f2d4a7c0cab858de45dddef4455b0f11&source=update",
 * "update_url": "http:\/\/fir.im\/c8ea",
 * "binary": {
 * "fsize": 8794425
 * }
 * }
 */

public class FirImBean implements Serializable {

    private String name;//APP名称
    private String version;//版本号
    private String changelog;//更改备注
    private long updated_at;//更新时间戳
    private String versionShort;//分级版本号
    private String build;
    private String installUrl;//更新url
    private String install_url;//更新url
    private String direct_install_url;//更新url
    private String update_url;//app fir url
    private Binary binary;//APP大小

    public class Binary implements Serializable {
        private long fsize;

        public long getFsize() {
            return fsize;
        }

        public void setFsize(long fsize) {
            this.fsize = fsize;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public Binary getBinary() {
        return binary;
    }

    public void setBinary(Binary binary) {
        this.binary = binary;
    }
}
