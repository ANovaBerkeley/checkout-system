require 'test_helper'

class MentorsControllerTest < ActionDispatch::IntegrationTest
  setup do
    @mentor = mentors(:one)
  end

  test "should get index" do
    get mentors_url
    assert_response :success
  end

  test "should get new" do
    get new_mentor_url
    assert_response :success
  end

  test "should create mentor" do
    assert_difference('Mentor.count') do
      post mentors_url, params: { mentor: { email: @mentor.email, name: @mentor.name, phone: @mentor.phone, upc: @mentor.upc } }
    end

    assert_redirected_to mentor_url(Mentor.last)
  end

  test "should show mentor" do
    get mentor_url(@mentor)
    assert_response :success
  end

  test "should get edit" do
    get edit_mentor_url(@mentor)
    assert_response :success
  end

  test "should update mentor" do
    patch mentor_url(@mentor), params: { mentor: { email: @mentor.email, name: @mentor.name, phone: @mentor.phone, upc: @mentor.upc } }
    assert_redirected_to mentor_url(@mentor)
  end

  test "should destroy mentor" do
    assert_difference('Mentor.count', -1) do
      delete mentor_url(@mentor)
    end

    assert_redirected_to mentors_url
  end
end
