import { tokenAxios } from 'api/tokenAPI';
import axios from 'axios';
import { completeMessage } from 'constants/completeMessage';
import { API_URL } from 'constants/envText';
import { headers } from 'constants/headers';
import { NavigateFunction } from 'react-router-dom';

export const addProjectMembers = async (
  projectId: number,
  memberIdList: number[],
  navigate: NavigateFunction
) => {
  // content 구성
  const memberList = memberIdList.map((id) => {
    return { employeeId: id };
  });
  const content: RegisterContent = {
    memberList: memberList,
  };

  try {
    const response = await tokenAxios.post(`${API_URL}/api/member/${projectId}/register`, content, {
      headers: headers,
    });
    if (response.data) {
      alert(completeMessage.REGISTER_MEMBER);
      navigate(`/project/${projectId}`);
      return;
    }
  } catch (error) {
    console.error(error);
    if (axios.isAxiosError(error)) {
      alert(error.response?.data.message);
    }
  }
};
