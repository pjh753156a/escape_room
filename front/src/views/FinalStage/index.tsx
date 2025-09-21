import { useState, ChangeEvent, useRef } from "react";
import "./style.css";
import { postFileUploadRequest } from "src/apis/auth";
import ResponseDto from "src/apis/response.dto";
import { useUserStore } from "src/stores";
import { useNavigate } from "react-router";
import { GAME_END_ABSOLUTE_PAGE } from "src/constant";

//                    type                    //
type AuthPage = 'final-stage-container' | 'final-stage-complete-container';

//                    interface                    //
interface Props {
    onLinkClickHandler: () => void
}

function NotMatchPasswd({ onLinkClickHandler }: Props) 
{
    const {finalStagePasswd} = useUserStore();
    const [file, setFile] = useState<File | null>(null);
    const [searchWord, setSearchWord] = useState<string>('');

    const postFileUploadResponse = (result:| ResponseDto | null) =>
    {
        const message =
            !result ? '업로드 실패 서버에 문제가 있습니다.' :
            result.code === 'DBE' ? '업로드 실패 서버에 문제가 있습니다.' :
            result.code === 'SU' ? '업로드 완료' :"";

        alert(message);
    }
    
    const handleUpload = async () => 
    {
        if (!file) 
        {
            alert("파일을 먼저 선택해주세요.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        postFileUploadRequest(formData).then(postFileUploadResponse);
    };
  
    const handleDownload = async () => 
    {
        const filename = prompt("다운로드할 파일명을 입력하세요:");
        window.open(`http://localhost:4000/api/vi/auth/download?file=${filename}`);
    };

    const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => 
    {
        if (e.target.files && e.target.files.length > 0) 
        {
            setFile(e.target.files[0]);
        }
    };

    const onSearchWordChangeHandler = (event: ChangeEvent<HTMLInputElement>) => 
    {
        const searchWord = event.target.value;
        setSearchWord(searchWord);
    };

    const onTryButtonClickHandler = () => 
    {
        if(!searchWord)
        {
            return;
        }

        if(finalStagePasswd==searchWord)
        {
            onLinkClickHandler();
            return;
        }
        else
        {
            alert("탈출 실패")
        }
    }

    const searchButtonClass = searchWord ? 'primary-button' : 'disable-button';

    return (
        <div className='final-stage-container'>
            <div className="final-stage-box">
                <h2 className="stage-title">🔐 Final Stage: File Portal</h2>
                <div>
                    To solve this challenge, you’ll need Burp Suite.
                    And then, you need to click 'Open Browser' in Burp Suite.
                    If you don’t have it installed, now’s the time to get it ready.
                    Your mission is simple:
                    Find the final_passwd hidden on the IP 192.168.110.67.
                    Good luck, agent. You're going to need it.
                </div>
                <input type="file" onChange={handleFileChange} className="file-input" />
                <div className="button-group">
                    <button onClick={handleUpload} className="dark-button">업로드</button>
                    <button onClick={handleDownload} className="dark-button">다운로드</button>
                </div>
                <div className='passwd-list-search-box'>
                    <div className='passwd-list-search-input-box'>
                        <input className='passwd-list-search-input' placeholder='input-passwd' value={searchWord} onChange={onSearchWordChangeHandler} />
                    </div>
                    <div className={searchButtonClass} onClick={onTryButtonClickHandler}>try</div>
                </div>
            </div>
        </div>
    );
}

function MatchPasswd()
{
    const videoRef = useRef<HTMLVideoElement>(null);
    const navigator = useNavigate();

    const handleLoadedMetadata = () => 
    {
        if (videoRef.current) 
        {
            // 예: 비디오의 중간부터 시작 (전체 길이의 절반부터)
            const startTime = videoRef.current.duration / 2;
            videoRef.current.currentTime = startTime;
        }
    };

    const handleEnded = () => 
    {
        navigator(GAME_END_ABSOLUTE_PAGE);
    };


    return (
        <div className="final-stage-complete-container">
            <video
                ref={videoRef}
                className="background-video"
                src={require('../../assets/image/Conan_Door.mp4')}
                autoPlay
                muted
                playsInline
                onLoadedMetadata={handleLoadedMetadata}
                onEnded={handleEnded}
            />
        </div>
    );
}

export default function FinalStage() 
{
    //                    event handler                    //
    const onLinkClickHandler = () => 
    {
        setFinalPageContents(<MatchPasswd/>);
    };

    const [FinalPageContents,setFinalPageContents] = useState<JSX.Element>(<NotMatchPasswd onLinkClickHandler={onLinkClickHandler}/>);

    return (
        <div>
            {FinalPageContents}
        </div>
    );
}

