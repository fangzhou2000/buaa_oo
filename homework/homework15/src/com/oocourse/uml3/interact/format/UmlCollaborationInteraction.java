package com.oocourse.uml3.interact.format;

import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.common.MessageSort;

/**
 * UML顺序图交互
 */
public interface UmlCollaborationInteraction {
    /**
     * 获取参与对象数
     * 指令：PTCP_OBJ_COUNT
     *
     * @param interactionName 交互名称
     * @return 参与对象数
     * @throws InteractionNotFoundException   交互未找到
     * @throws InteractionDuplicatedException 交互重名
     */
    int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException;

    /**
     * 获取对象的进入消息数
     * 指令：INCOMING_MSG_COUNT
     *
     * @param interactionName 交互名称
     * @param lifelineName    消息名称
     * @return 进入消息数
     * @throws InteractionNotFoundException   交互未找到
     * @throws InteractionDuplicatedException 交互重名
     * @throws LifelineNotFoundException      生命线未找到
     * @throws LifelineDuplicatedException    生命线重名
     */
    int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException;

    /**
     * 获取对象发出的某个类别的消息数
     * 指令：SENT_MESSAGE_COUNT
     *
     * @param interactionName 交互名称
     * @param lifelineName    消息名称
     * @param sort            消息类别
     * @return 发出的某个类别的消息数
     * @throws InteractionNotFoundException   交互未找到
     * @throws InteractionDuplicatedException 交互重名
     * @throws LifelineNotFoundException      生命线未找到
     * @throws LifelineDuplicatedException    生命线重名
     */
    int getSentMessageCount(String interactionName, String lifelineName, MessageSort sort)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException;
}
