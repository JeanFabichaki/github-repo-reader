package com.trustly.challenger.githubreporeader.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File {

    private String name;
    private Long lines;
    private Long bytes;

    public String getExtension() {
        int lastIndex = this.name.lastIndexOf('.');

        if (lastIndex == -1) {
            lastIndex = this.name.lastIndexOf('/');
        }
        return this.name.substring(lastIndex + 1);
    }
}
