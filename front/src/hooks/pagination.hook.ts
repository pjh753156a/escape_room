import { Console } from "node:console";
import { useEffect, useState } from "react";

const usePagination = <T>(countPerPage: number, countPerSection: number) => {

    const [viewList, setViewList] = useState<T[]>([]);
    const [boardList, setBoardList] = useState<T[]>([]);
    const [totalPage, setTotalPage] = useState<number>(1);
    const [pageList, setPageList] = useState<number[]>([1]);
    const [totalLenght, setTotalLength] = useState<number>(0);
    const [currentPage, setCurrentPage] = useState<number>(1);
    const [totalSection, setTotalSection] = useState<number>(1);
    const [currentSection, setCurrentSection] = useState<number>(1);
    
    const changePage = (boardList: T[], totalLenght: number) => 
    {
        if(!currentPage) return;
        const startIndex = (currentPage - 1) * countPerPage;
        // startIndex = 4
        let endIndex = currentPage * countPerPage;
        // endIndex = 8
        if (endIndex > totalLenght-1) endIndex = totalLenght;
        const viewList = boardList.slice(startIndex, endIndex);
        setViewList(viewList);
    };
    
    const changeSection = (totalPage: number) => 
    {
        if(!currentSection) return; 
        const startPage = (currentSection * countPerSection) - (countPerSection - 1);
        // startPage = 1
        let endPage = currentSection * countPerSection;
        // endPage = 2
        if (endPage > totalPage) endPage = totalPage;
        const pageList: number[] = [];
        for (let page = startPage; page <= endPage; page++) pageList.push(page);
        setPageList(pageList);
    };
    
    const changeBoardList = (boardList: T[], isAdminToggleOn?: boolean, isWriterToggleOn?: boolean, loginUserId?: string) => {
        
        if(isAdminToggleOn) boardList = boardList.filter((board: any) => {
            if('status' in board) return !board.status;
            return false;
        });

        if(isWriterToggleOn) boardList = boardList.filter((board: any) => {
            if(board.writerId == loginUserId) return true;
            return false;
        });

        boardList.map((board: any) => {
            board.writerId = board.writerId.substring(0, 1)+"*".repeat(board.writerId.length-1);
        });

        setBoardList(boardList);

        const totalLenght = boardList.length;
        setTotalLength(totalLenght);
        
        //  totalPage   =    4
        const totalPage = Math.floor((totalLenght - 1) / countPerPage) + 1;
        setTotalPage(totalPage);
        
        //  totalSection    =  2
        const totalSection = Math.floor((totalPage - 1) / countPerSection) + 1;
        setTotalSection(totalSection);
        
        changePage(boardList, totalLenght);
        changeSection(totalPage);
    };
    
    const onPageClickHandler = (page: number) => {
        setCurrentPage(page);
    };
    
    const onPreSectionClickHandler = () => {
        if (currentSection <= 1) return;
        setCurrentSection(currentSection - 1);
        setCurrentPage((currentSection - 1) * countPerSection);
    };
    
    const onNextSectionClickHandler = () => 
    {
        if (currentSection === totalSection) return;
        setCurrentSection(currentSection + 1);
        setCurrentPage(currentSection * countPerSection + 1);
    }; 
    
    useEffect(() => 
    {
        if (!boardList.length) return;
        changePage(boardList, totalLenght);
    }, [currentPage]);
    
    useEffect(() => {
        if (!boardList.length) return;
        changeSection(totalPage);
    }, [currentSection]);
    
    return{
        viewList,
        pageList,
        totalPage,
        currentPage,
        totalLenght,

        setCurrentPage,
        setCurrentSection,

        changeBoardList,

        onPageClickHandler,
        onPreSectionClickHandler,
        onNextSectionClickHandler
    };
}

export default usePagination
{/* 최종완료 */}