package com.example.DeiveC.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private String ID;
    private String name;
    private String url;
    private String type;
    private long size;


}
