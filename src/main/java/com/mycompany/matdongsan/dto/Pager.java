package com.mycompany.matdongsan.dto;

import java.util.stream.IntStream;

import lombok.Data;

@Data
public class Pager {
    private int totalRows;      // 전체 행 수
    private int totalPageNo;    // 전체 페이지 수
    private int totalGroupNo;   // 전체 그룹 수
    private int startPageNo;    // 그룹의 시작 페이지 번호
    private int endPageNo;      // 그룹의 끝 페이지 번호
    private int[] pageArray;    // startPageNo ~ endPageNo 배열
    private int pageNo;         // 현재 페이지 번호
    private int pagesPerGroup;  // 그룹당 페이지 수
    private int groupNo;        // 현재 그룹 번호
    private int rowsPerPage;    // 페이지당 행 수
    private int offset;         // PostgreSQL용 OFFSET
    private int limit;          // PostgreSQL용 LIMIT

    // 인피니티 스크롤용
    public Pager(int rowsPerPage, int pageNo, int totalRows) {
        this.rowsPerPage = rowsPerPage;
        this.pageNo = pageNo;
        this.totalRows = totalRows;

        this.offset = (pageNo - 1) * rowsPerPage;
        this.limit = rowsPerPage;
    }

    // 페이저용
    public Pager(int rowsPerPage, int pagesPerGroup, int totalRows, int pageNo) {
        this.rowsPerPage = rowsPerPage;
        this.pagesPerGroup = pagesPerGroup;
        this.totalRows = totalRows;
        this.pageNo = pageNo;

        totalPageNo = totalRows / rowsPerPage;
        if (totalRows % rowsPerPage != 0) totalPageNo++;

        totalGroupNo = totalPageNo / pagesPerGroup;
        if (totalPageNo % pagesPerGroup != 0) totalGroupNo++;

        groupNo = (pageNo - 1) / pagesPerGroup + 1;

        startPageNo = (groupNo - 1) * pagesPerGroup + 1;

        endPageNo = startPageNo + pagesPerGroup - 1;
        if (groupNo == totalGroupNo) endPageNo = totalPageNo;

        pageArray = IntStream.rangeClosed(startPageNo, endPageNo).toArray();

        offset = (pageNo - 1) * rowsPerPage;
        limit = rowsPerPage;
    }
}
