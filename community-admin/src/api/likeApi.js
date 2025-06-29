// api/likeApi
import request from "../../../community-ui/src/utils/request.js";

export const addLike = (data) => {
    return request.put(`/like/addLike/${data}`).then(res => {
        if (res.code === 200) {
            return res
        }
    })
}
