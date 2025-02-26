import { GetLocalDataResponseDto, GetRatioDataResponseDto } from "src/apis/estate/dto/response/index";

import axios from "axios";
import { bearerAuthorization, requestErrorHandler, requestHandler } from "src/apis/index";

import { GET_LOCAL_DATA_URL, GET_RATIO_DATA_URL } from "src/constant";

//                 function : 지역 데이터 불러오기 API 함수                 //
export const getLocalDataRequest = async (local:string,accessToken:string) => 
{
    const result = await axios.get(GET_LOCAL_DATA_URL(local), bearerAuthorization(accessToken))
        .then(requestHandler<GetLocalDataResponseDto>)
        .catch(requestErrorHandler);
    return result;
};

//                 function : 비율 데이터 불러오기 API 함수                 //
export const getRatioDataRequest = async (local:string,accessToken:string) =>
{
    const result = await axios.get(GET_RATIO_DATA_URL(local), bearerAuthorization(accessToken))
        .then(requestHandler<GetRatioDataResponseDto>)
        .catch(requestErrorHandler);
    return result;
};
/* 최종완료 */