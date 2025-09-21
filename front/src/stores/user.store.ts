import { create } from "zustand";

interface UserStore
{
    loginUserId: string,
    setLoginUserId: (loginUserId: string) => void,
    loginUserRole: string,
    setLoginUserRole: (loginUserRole:string) => void,
    loginUserEmail: string,
    setLoginUserEmail: (loginUserEmail:string) => void,
    authenticationPasswd: string,
    finalStagePasswd:string,
}

const useUserStore = create<UserStore>(set => ({
    loginUserId: '',
    setLoginUserId: (loginUserId:string) => set(state => ({ ...state, loginUserId})),
    loginUserRole: '',
    setLoginUserRole: (loginUserRole:string) => set(state => ({...state, loginUserRole})),
    loginUserEmail: '',
    setLoginUserEmail:(loginUserEmail:string) => set(state => ({...state, loginUserEmail})),
    authenticationPasswd: "5725",
    finalStagePasswd:"black_label_project1"
}));

export default useUserStore;
/* 최종완료 */