package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-09
 **/

@Data
public class ArticleSearchParam {
 private String  title;
 private Long  categoryId;
 private Long  tagId;
 private Integer  status;
 private String  startTime;
 private String  endTime;
 private Integer  pageNum;
 private Integer  pageSize;

}
