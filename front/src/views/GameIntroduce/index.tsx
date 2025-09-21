import { useState, useEffect } from 'react';
import { AUTH_ABSOLUTE_PATH } from 'src/constant';
import '../GameIntroduce/style.css';
import { useNavigate } from 'react-router';

const fullText = `한국의 손꼽히는 정보보안 전문가 A는, 뛰어난 방어 실력으로 많은 블랙해커들의 공격을 막아낸 인물로 유명하다.
그의 철통같은 보안 덕분에 수많은 해커들이 시스템 권한을 탈취하려다 실패를 맛보았다.
이에 분노한 블랙해커들은 복수를 계획하게 되고, 다음 공격 대상 사이트를 노리면서도 A에게 또다시 가로막힐까 두려워한다.

그래서 그들은 의논 끝에 A를 제거하기로 결정한다.

마침 A는 이사를 준비 중이었고, 새 집을 알아보러 부동산에 방문하게 된다.
그곳에서 직원이 건넨 커피 한 잔을 마신 A는, 갑자기 의식을 잃는다.

눈을 떴을 때, 그는 어딘가의 방 안에 갇혀 있었다.
문은 모두 잠겨 있었고, 방 안에는 컴퓨터 한 대가 놓여 있었고,
그 컴퓨터에는 웹사이트 하나가 떠 있었다.
화면 한가운데에는 해커의 메시지가 남겨져 있었다.

"사이트 한번 뚫어봐 그럼 방에서 나갈 수 있어"

이 웹사이트의 수수께끼를 풀어야 방문이 열린다.
공격자가 만든 함정 속 퍼즐을 직접 풀어야만 하는데....`;

export default function GameIntroduce() {
  const navigator = useNavigate();
  const [displayText, setDisplayText] = useState('');
  const [index, setIndex] = useState(0);
  const [started, setStarted] = useState(false);

  useEffect(() => {
    let timer: NodeJS.Timeout;
    if (started && index < fullText.length) {
      timer = setTimeout(() => {
        setDisplayText((prev) => prev + fullText[index]);
        setIndex((prev) => prev + 1);
      }, 50); // 글자 나타나는 속도 조절
    }
    return () => clearTimeout(timer);
  }, [index, started]);

  const handleClick = () => {
    if (!started) {
      setStarted(true);
    } else if (index > 0) {
      navigator(AUTH_ABSOLUTE_PATH);
    }
  };

  return (
    <div className="gameintro" onClick={handleClick}>
      {displayText}
    </div>
  );
}
