package xuehai.vo;

import java.util.Date;

public class TimeLine {
    private Long content1Id;
    private String  content1;
    private Long content2Id;
    private String content2;
    private Long content3Id;
    private String content3;
    private Integer contentType;
    private Date time;

    public Long getContent1Id() {
        return content1Id;
    }

    public void setContent1Id(Long content1Id) {
        this.content1Id = content1Id;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public Long getContent2Id() {
        return content2Id;
    }

    public void setContent2Id(Long content2Id) {
        this.content2Id = content2Id;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public Long getContent3Id() {
        return content3Id;
    }

    public void setContent3Id(Long content3Id) {
        this.content3Id = content3Id;
    }

    public String getContent3() {
        return content3;
    }

    public void setContent3(String content3) {
        this.content3 = content3;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
