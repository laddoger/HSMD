import request from '@/utils/request'

// 查询风速数据列表
export function listSpeeddata(query) {
  return request({
    url: '/environment/speeddata/list',
    method: 'get',
    params: query
  })
}

// 查询风速数据详细
export function getSpeeddata(id) {
  return request({
    url: '/environment/speeddata/' + id,
    method: 'get'
  })
}

// 新增风速数据
export function addSpeeddata(data) {
  return request({
    url: '/environment/speeddata',
    method: 'post',
    data: data
  })
}

// 修改风速数据
export function updateSpeeddata(data) {
  return request({
    url: '/environment/speeddata',
    method: 'put',
    data: data
  })
}

// 删除风速数据
export function delSpeeddata(id) {
  return request({
    url: '/environment/speeddata/' + id,
    method: 'delete'
  })
}
