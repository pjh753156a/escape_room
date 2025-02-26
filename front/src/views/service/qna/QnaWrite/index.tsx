import { ChangeEvent, useEffect, useRef, useState } from 'react';
import { useCookies } from 'react-cookie';
import { useNavigate } from 'react-router';

import { useUserStore } from 'src/stores';

import { PostBoardRequestDto } from 'src/apis/board/dto/request';
import ResponseDto from 'src/apis/response.dto';

import { postBoardRequest } from 'src/apis/board';

import { QNA_LIST_ABSOLUTE_PATH } from 'src/constant';

import './style.css';

//            component               //
export default function  QnaWrite()
{
  
  //                  state                     //
  const [cookies] = useCookies();
  const {loginUserRole} = useUserStore();
  const [title,setTitle] = useState<string>('');
  const [contents,setContents] = useState<string>('');
  const contentsRef = useRef<HTMLTextAreaElement | null>(null);

  //                function                    //
  const navigator = useNavigate();
  
  const postBoardResponse = (result: ResponseDto | null) => 
  {
    const message =
      !result ? '서버에 문제가 있습니다.' :
      result.code === 'VF' ? '제목과 내용을 모두 입력해주세요.' :
      result.code === 'AF' ? '권한이 없습니다.' :
      result.code === 'DBE' ? '서버에 문제가 있습니다.' : '';

    if(!result || result.code !== 'SU')
    {
      alert(message);
      return;
    }

    navigator(QNA_LIST_ABSOLUTE_PATH);
  };
  
  //                 event handler                //
  const onTitleChangeHandler = (event: ChangeEvent<HTMLInputElement>) => 
  {
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
  
  const onPostButtonClickHandler = () => {
    if(!title.trim() || !contents.trim()) return;

    const requestBody: PostBoardRequestDto = {title,contents}
    if(!cookies.accessToken) return;

    postBoardRequest(requestBody,cookies.accessToken).then(postBoardResponse);
  }
  
  //                effect                  //
  useEffect(() => {
    if(loginUserRole === 'ROLE_ADMIN')
    {
      navigator(QNA_LIST_ABSOLUTE_PATH);
      return;
    }
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
        <div className='primary-button' onClick={onPostButtonClickHandler}>올리기</div>
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
  );
}
{/* 최종완료 */}