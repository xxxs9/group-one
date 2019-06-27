package com.heeexy.example.util.model;

/**
 * @author: linchen
 * @description:
 * @date: 2019/6/26
 * @Version:
 */
public class Tag {
    private Integer id;
    private String tagName;
    private Integer parentId;
    private Integer rank;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getId() {
        return id;
    }

    public String getTagName() {
        return tagName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getRank() {
        return rank;
    }

    public Tag() {
    }

    public Tag(Integer id, String tagName, Integer parentId, Integer rank) {
        this.id = id;
        this.tagName = tagName;
        this.parentId = parentId;
        this.rank = rank;
    }
}
