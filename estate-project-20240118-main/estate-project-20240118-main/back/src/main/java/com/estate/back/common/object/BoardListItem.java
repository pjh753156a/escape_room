package com.estate.back.common.object;

import java.util.ArrayList;
import java.util.List;

import com.estate.back.common.util.ChangeDateFormatUtil;
import com.estate.back.entity.BoardEntity;

import lombok.Getter;

@Getter
public class BoardListItem
{
    private Boolean status;
    private String title;
    private String writerId;
    private String writeDatetime;
    private Integer viewCount;
    private Integer receptionNumber;
    
    private BoardListItem(BoardEntity boardEntity) throws Exception
    {
        String writeDatetime = ChangeDateFormatUtil.changeYYMMDD(boardEntity.getWriteDatetime());
        String writerId = boardEntity.getWriterId();
        writerId = writerId.substring(0, 1)+
        "*".repeat(writerId.length()-1);

        this.writerId = writerId;
        this.writeDatetime = writeDatetime;
        this.title = boardEntity.getTitle();
        this.status = boardEntity.getStatus();
        this.viewCount = boardEntity.getViewCount();
        this.receptionNumber = boardEntity.getReceptionNumber();
    }
    
    public static List<BoardListItem> getList(List<BoardEntity> boardEntities) throws Exception
    {
        List<BoardListItem> boardList = new ArrayList<>();

        for(BoardEntity boardEntity:boardEntities)
        {
            BoardListItem boardListItem = new BoardListItem(boardEntity);
            boardList.add(boardListItem);
        }

        return boardList;
    }
}
/* // */