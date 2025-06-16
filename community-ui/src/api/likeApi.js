// api/likeApi
import request from "@/utils/request.js";

export const addLike = (data) => {
    return request.put(`/like/addLike/${data}`).then(res => {
        if (res.code === 200) {
            return res
        }
    })
}
