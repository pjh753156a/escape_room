import { ChangeEvent, KeyboardEvent } from "react";
import './style.css';

export interface InputBoxProps 
{
    label: string;
    value: string;
    error?: boolean;
    message?: string;
    placeholder: string;
    buttonTitle?: string;
    buttonStatus?: boolean;
    type: 'text' | 'password';
    onButtonClickHandler?: () => void;
    onChangeHandler: (event: ChangeEvent<HTMLInputElement>) => void;
    onKeydownHandler? : (event:KeyboardEvent<HTMLInputElement>) => void;
}

export default function InputBox({ label, type, value, placeholder, onChangeHandler, buttonTitle, buttonStatus, onButtonClickHandler, message, error,onKeydownHandler }: InputBoxProps) 
{
// label="인증번호" type="text"
// value={authNumber} placeholder="인증번호 4자리를 입력해주세요"
// onChangeHandler={onAuthNumberChangeHandler}
// buttonTitle="인증 확인" buttonStatus={authNumberButtonStatus}
// onButtonClickHandler={onAuthNumberButtonClickHandler}
// message={authNumberMessage} error={isAuthNumberError}

    const buttonClass = buttonStatus ? 'input-primary-button' : 'input-disable-button';
    const messageClass = 'input-message ' + (error ? 'error' : 'primary');

    return (
        <div className="input-box">
            <div className="input-label label">{label}</div>
            <div className="input-content-box">
                <input
                    className="input"
                    type={type}
                    value={value}
                    placeholder={placeholder}
                    onChange={onChangeHandler}
                    onKeyDown={onKeydownHandler}
                />
                { buttonTitle &&
                <div className={buttonClass} onClick={onButtonClickHandler}>
                    {buttonTitle}
                </div>
                }
            </div>
            <div className={messageClass}>
                {message}
            </div>
        </div>
    );
}
{/* 최종완료 */}