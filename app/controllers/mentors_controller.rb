class MentorsController < ApplicationController
  before_action :set_mentor, only: [:show, :edit, :update, :destroy]

  # GET /mentors
  # GET /mentors.json
  def index
    @mentors = Mentor.all
  end

  # GET /mentors/1
  # GET /mentors/1.json
  def show
  end

  # GET /mentors/new
  def new
    @mentor = Mentor.new
  end

  # GET /mentors/1/edit
  def edit
  end

  # POST /mentors
  # POST /mentors.json
  def create
    @mentor = Mentor.new(mentor_params)
    if @mentor.save
      redirect_to :root, notice: 'Mentor was successfully created.'
    else
      render :new
    end
  end

  def import
    begin
      Mentor.import(params[:file])
      redirect_to :root, notice: 'Mentors successfully created from CSV.'
    rescue
      redirect_to :root, notice: 'Invalid CSV format.'
    end
  end

  # PATCH/PUT /mentors/1
  # PATCH/PUT /mentors/1.json
  def update
    if @mentor.update(mentor_params)
      redirect_to :root, notice: 'Mentor was successfully updated.'
    else
      render :edit
    end
  end

  # DELETE /mentors/1
  # DELETE /mentors/1.json
  def destroy
    @mentor.destroy
    redirect_to mentors_url, notice: 'Mentor was successfully destroyed.'
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_mentor
      @mentor = Mentor.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def mentor_params
      params.require(:mentor).permit(:name, :email, :phone, :upc)
    end
end
