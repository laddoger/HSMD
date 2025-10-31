import request from '@/utils/request'

// 查询喷雾阀门开度列表
export function listOpening(query) {
  return request({
    url: '/environment/opening/list',
    method: 'get',
    params: query
  })
}

// 查询喷雾阀门开度详细
export function getOpening(sectorId) {
  return request({
    url: '/environment/opening/' + sectorId,
    method: 'get'
  })
}

// 新增喷雾阀门开度
export function addOpening(data) {
  return request({
    url: '/environment/opening',
    method: 'post',
    data: data
  })
}

// 修改喷雾阀门开度
export function updateOpening(data) {
  return request({
    url: '/environment/opening',
    method: 'put',
    data: data
  })
}

// 删除喷雾阀门开度
export function delOpening(sectorId) {
  return request({
    url: '/environment/opening/' + sectorId,
    method: 'delete'
  })
}
