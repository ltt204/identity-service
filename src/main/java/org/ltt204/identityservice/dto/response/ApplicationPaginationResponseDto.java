package org.ltt204.identityservice.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class ApplicationPaginationResponseDto <T> {
    private int size;
    private long totalElements;
    private int totalPages;
    private List<T> content;
    private int currentPage;

    public ApplicationPaginationResponseDto(Page<T> pageResult){
        this.setSize(pageResult.getSize());
        this.setTotalElements(pageResult.getTotalElements());
        this.setTotalPages(pageResult.getTotalPages());
        this.setCurrentPage(pageResult.getPageable().getPageNumber());
        this.setContent(pageResult.getContent());
    }

    public ApplicationPaginationResponseDto<T> get(){
        return this;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
