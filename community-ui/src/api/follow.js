// api/follow.js

import request from "@/utils/request.js";

export const followAuthor = (data) => {
    return request.post(`/follow/followAuthor`, data)
}
