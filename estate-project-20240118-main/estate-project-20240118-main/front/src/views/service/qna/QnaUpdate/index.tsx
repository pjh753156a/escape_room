import { ChangeEvent, useEffect, useRef, useState } from 'react';
import { useCookies } from 'react-cookie';
import { useNavigate, useParams } from 'react-router';

import { useUserStore } from 'src/stores';

import { PutBoardRequestDto } from 'src/apis/board/dto/request';
import { GetBoardResponseDto } from 'src/apis/board/dto/response';
import ResponseDto from 'src/apis/response.dto';

import { PutBoardRequest, getBoardRequest } from 'src/apis/board';

import { QNA_DEATIL_ABSOLUTE_PATH, QNA_LIST_ABSOLUTE_PATH } from 'src/constant';

import './style.css';

//                  component                 //
export default function QnaUpdate()
{
  
  //                  state                     //
  const [title,setTitle] = useState<string>('');
  const [contents,setContents] = useState<string>('');
  
  const [cookies] = useCookies();
  const {receptionNumber} = useParams();
  const {loginUserId,loginUserRole} = useUserStore();
  const contentsRef = useRef<HTMLTextAreaElement | null>(null);

  //                function                    //
  const navigator = useNavigate();
  
  const getBoardResponse = (result:GetBoardResponseDto | ResponseDto | null) => 
  {
    const message =
      !result ? '서버에 문제가 있습니다.':
      result.code === 'VF' ? '올바르지 않은 접수 번호 입니다.' :
      result.code === 'AF' ? '인증에 실패했습니다.' :
      result.code === 'NB' ? '존재하지 않는 접수 번호 입니다.':
      result.code === 'DBE' ? '서버에 문제가 있습니다.' : '';

    if(!result || result.code !== 'SU')
    {
      alert(message);
      navigator(QNA_LIST_ABSOLUTE_PATH);
      return;
    }

    const {writerId, title, contents, status} = result as GetBoardResponseDto;
    if(writerId !== loginUserId)
    {
      alert('권한이 없습니다.');
      navigator(QNA_LIST_ABSOLUTE_PATH);
      return;
    }
    if(status)
    {
      alert('답변이 완료된 게시물입니다.');
      navigator(QNA_LIST_ABSOLUTE_PATH);
      return;
    }

    setTitle(title);
    setContents(contents);
  };
  
  const putBoardResponse = (result:ResponseDto | null) => {
      const message =
        !result ? '서버에 문제가 있습니다.' :
        result.code === 'AF' ? '권한이 없습니다.' :
        result.code === 'VF' ? '모든 값을 입력해주세요.' :
        result.code === 'NB' ? '존재하지 않는 접수 번호 입니다.' :
        result.code === 'WC' ? '이미 답글이 작성되어있습니다.' :
        result.code === 'DBE' ? '서버에 문제가 있습니다.' : '';

      if(!result || result.code !== 'SU')
      {
        alert(message);
        return;
      }

      if(!receptionNumber) return;
      navigator(QNA_DEATIL_ABSOLUTE_PATH(receptionNumber));
  };
  
  //                 event handler                //
  const onTitleChangeHandler = (event: ChangeEvent<HTMLInputElement>) => {
      const title = event.target.value;
      setTitle(title);
  };
  
  const onContentsChangeHandler = (event: ChangeEvent<HTMLTextAreaElement>) => 
  {
      const contents = event.target.value;
      if(contents.length>1000) return;
      setContents(contents);
      
      if(!contentsRef.current) return;
        contentsRef.current.style.height = 'auto';
        contentsRef.current.style.height = `${contentsRef.current.scrollHeight}px`;
  };
  
  const onUpdateButtonClickHandler = () => 
  {
    if(!cookies.accessToken || !receptionNumber) return;
    if(!title.trim() || !contents.trim()) return;

    const requestBody: PutBoardRequestDto = {title,contents};
    PutBoardRequest(receptionNumber,requestBody,cookies.accessToken).then(putBoardResponse);
  };
  
  //                effect                  //
  let effectFlag = false;
  useEffect(()=>{
    if(!receptionNumber || !cookies.accessToken) return;
    if(!loginUserRole) return;
    if(effectFlag) return;
    effectFlag = true;
    if(loginUserRole !== 'ROLE_USER')
    {
        navigator(QNA_LIST_ABSOLUTE_PATH);
        return;
    }
    getBoardRequest(receptionNumber,cookies.accessToken).then(getBoardResponse);
  },[loginUserRole])
  
  //                render                  //
  return (
    <div id="qna-write-wrapper">
      <div className='qna-write-top'>
        <div className='qna-write-title-box'>
          <input className='qna-write-title-input'
            value={title} onChange={onTitleChangeHandler}
          placeholder='제목을 입력해주세요.'/>
        </div>
        <div className='primary-button' onClick={onUpdateButtonClickHandler}>수정</div>
      </div>
      <div className='qna-write-contents-box'>
        <textarea ref={contentsRef}
        className='qna-write-contents-textarea'
        placeholder='내용을 입력해주세요. / 1000자'
        maxLength={1000}
        value={contents}
        onChange={onContentsChangeHandler}/>
      </div>
    </div>
  )
}
{/* // */}