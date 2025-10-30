import request from '@/utils/request'

// 查询回水管温度列表
export function listRpiptemp(query) {
  return request({
    url: '/environment/rpiptemp/list',
    method: 'get',
    params: query
  })
}

// 查询回水管温度详细
export function getRpiptemp(sectorId) {
  return request({
    url: '/environment/rpiptemp/' + sectorId,
    method: 'get'
  })
}

// 新增回水管温度
export function addRpiptemp(data) {
  return request({
    url: '/environment/rpiptemp',
    method: 'post',
    data: data
  })
}

// 修改回水管温度
export function updateRpiptemp(data) {
  return request({
    url: '/environment/rpiptemp',
    method: 'put',
    data: data
  })
}

// 删除回水管温度
export function delRpiptemp(sectorId) {
  return request({
    url: '/environment/rpiptemp/' + sectorId,
    method: 'delete'
  })
}
