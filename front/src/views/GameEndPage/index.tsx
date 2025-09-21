import { useState, useEffect } from 'react';
import { GAME_INTRODUCE_ABSOLUTE_PATH } from 'src/constant';
import '../GameEndPage/style.css';
import { useNavigate } from 'react-router';

const fullText = `갑자기 방문이 열렸다. 사무실에는 아무도 없었다....
                                                                    
방 탈출에 성공하였습니다. 고로 1차 프로젝트가 무사히 끝났습니다.
2차 3차 프로젝트도 무사히 끝내서 악질 해커들을 막으로 가셔야죠??`;

export default function GameEndPage() 
{
    const navigator = useNavigate();
    const [displayText, setDisplayText] = useState('');
    const [index, setIndex] = useState(0);
    const [started, setStarted] = useState(false);

    useEffect(() => 
    {
        let timer: NodeJS.Timeout;
        if (started && index < fullText.length) 
        {
            timer = setTimeout(() => 
            {
                setDisplayText((prev) => prev + fullText[index]);
                setIndex((prev) => prev + 1);
            }, 50); // 글자 나타나는 속도 조절
        }
        return () => clearTimeout(timer);
    }, [index, started]);

    const handleClick = () => 
    {
        if (!started) 
        {
            setStarted(true);
        } 
        else if (index > 0)
        {
            navigator(GAME_INTRODUCE_ABSOLUTE_PATH);
        }
    };

    return (
        <div className="gameEndPage" onClick={handleClick}>
            <div className="gameEndTextBox">
                {displayText}
            </div>
        </div>
    );
}
