import { ChangeEvent, useEffect, useState } from 'react';
import { useCookies } from 'react-cookie';
import { useNavigate } from 'react-router';

import { usePagination } from 'src/hooks';
import { useUserStore } from 'src/stores';

import { GetSearchBoardListResponseDto } from 'src/apis/board/dto/response';
import ResponseDto from 'src/apis/response.dto';
import { BoardListItem } from 'src/types';

import { getSearchBoardListRequest } from 'src/apis/board';

import { AUTH_ABSOLUTE_PATH, COUNT_PER_PAGE, COUNT_PER_SECTION, QNA_DEATIL_ABSOLUTE_PATH, QNA_WRITE_ABSOLUTE_PATH } from 'src/constant';

import './style.css';
import { deleteUserInfoRequest } from 'src/apis/auth';
import { DeleteUserRequestDto } from 'src/apis/auth/dto/request';


//                    component                    //
export default function MyInfo()
{
    //                    state                    //
    const {loginUserId, loginUserEmail} = useUserStore();


    const [cookies] = useCookies();

    //                    function                    //
    const navigator = useNavigate();
    
    const deleteUserInfoRequestResponse = (result: GetSearchBoardListResponseDto | ResponseDto | null) => {

        const message =
            !result ? '서버에 문제가 있습니다.' :
            result.code === 'VF' ? '검색어를 입력하세요.' :
            result.code === 'AF' ? '인증에 실패했습니다.' :
            result.code === 'DBE' ? '서버에 문제가 있습니다.' : '';

        if (!result || result.code !== 'SU') {
            alert(message);
            if (result?.code === 'AF') navigator(AUTH_ABSOLUTE_PATH);
            return;
        }

        alert("회원탈퇴가 성공적으로 진행되었습니다.");
        navigator(AUTH_ABSOLUTE_PATH);
    };
    
    //                event handler                    //
    const onWithdrawButtonClickHandler = () => 
    {
        alert("You can’t do that.")
        return;

        const isConfirmed = window.confirm("정말로 회원 탈퇴를 진행하시겠습니까?\n회원님의 모든 활동 내역이 사라집니다.");
        if (!isConfirmed || !cookies.accessToken) return;

        const requestBody: DeleteUserRequestDto =
        {
            userId: loginUserId,
            userEmail: loginUserEmail
        }

        deleteUserInfoRequest(requestBody, cookies.accessToken).then(deleteUserInfoRequestResponse);
    };
    
    //                    effect                    //

    //                    render                    //
    return (
        <div id='qna-list-wrapper2'>
            <div className='qna-list-table2'>
                <div className='qna-list-table-th2'>
                    <div>나의 아이디 <span style={{ marginLeft: '50px' }}>{loginUserId}</span></div>
                </div>
            </div>
            <div className='qna-list-table2'>
                <div className='qna-list-table-th2'>
                    <div>나의 이메일  <span style={{ marginLeft: '50px' }}>{loginUserEmail}</span></div>
                </div>
            </div>
            <div className='qna-list-search-box2'>
                <div className="withdraw-button" onClick={onWithdrawButtonClickHandler}>회원 탈퇴</div>
            </div>
        </div>
    );
}
{/* 최종완료 */}